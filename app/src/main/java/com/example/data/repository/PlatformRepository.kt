package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.AcademicResource
import com.example.data.model.Course
import com.example.data.model.DepartmentAnnouncement
import com.example.data.model.ExcosMember
import com.example.data.model.Level
import com.example.data.model.ModerationStatus
import com.example.data.model.ResourceReport
import com.example.data.model.ResourceType
import com.example.data.model.SuggestionCategory
import com.example.data.model.SuggestionComplaint
import com.example.data.model.SuggestionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlatformRepository(
    private val database: AppDatabase,
    private val context: Context
) {
    private val resourceDao = database.resourceDao()
    private val suggestionDao = database.suggestionDao()
    private val courseDao = database.courseDao()
    private val departmentDao = database.departmentDao()

    // 1. Resources
    fun getApprovedResources(): Flow<List<AcademicResource>> = resourceDao.getAllApproved()

    fun getApprovedResourcesByLevel(level: String): Flow<List<AcademicResource>> =
        resourceDao.getApprovedByLevel(level)

    fun getAllResourcesForModeration(): Flow<List<AcademicResource>> =
        resourceDao.getAllForModeration()

    fun getPendingResourcesForModeration(): Flow<List<AcademicResource>> =
        resourceDao.getPendingForModeration()

    suspend fun submitResource(
        title: String,
        description: String,
        courseCode: String,
        courseTitle: String,
        level: Level,
        resourceType: ResourceType,
        fileFormat: String,
        fileSizeMb: Double,
        contributorName: String,
        contributorMatric: String = "",
        previewSummary: String = ""
    ): Long {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val dateString = dateFormat.format(Date())

        val resource = AcademicResource(
            title = title,
            description = description,
            courseCode = courseCode,
            courseTitle = courseTitle,
            level = level.name,
            resourceType = resourceType.name,
            fileFormat = fileFormat,
            fileSizeMb = fileSizeMb,
            uploadDate = dateString,
            contributorName = contributorName.ifBlank { "Anonymous Student" },
            contributorMatric = contributorMatric,
            moderationStatus = ModerationStatus.PENDING.name,
            moderationNote = "Awaiting EXCOS verification for syllabus relevance & file safety.",
            previewSummary = previewSummary.ifBlank { "Contributed academic resource awaiting EXCOS review." }
        )
        return resourceDao.insert(resource)
    }

    suspend fun updateModeration(id: Long, status: ModerationStatus, note: String = "") {
        resourceDao.updateModerationStatus(id, status.name, note)
    }

    suspend fun incrementDownload(id: Long) {
        resourceDao.incrementDownloadCount(id)
    }

    suspend fun reportResource(
        resourceId: Long,
        resourceTitle: String,
        courseCode: String,
        reason: String,
        details: String
    ): Long {
        resourceDao.incrementFlagCount(resourceId)
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val report = ResourceReport(
            resourceId = resourceId,
            resourceTitle = resourceTitle,
            courseCode = courseCode,
            reason = reason,
            details = details,
            reportDate = dateFormat.format(Date())
        )
        return resourceDao.insertReport(report)
    }

    fun getAllReports(): Flow<List<ResourceReport>> = resourceDao.getAllReports()

    suspend fun resolveReport(reportId: Long, adminName: String) {
        resourceDao.resolveReport(reportId, adminName)
    }

    // 2. Suggestions & Complaints
    fun getAllSuggestions(): Flow<List<SuggestionComplaint>> = suggestionDao.getAllSuggestions()

    suspend fun submitSuggestion(
        subject: String,
        category: SuggestionCategory,
        message: String,
        studentName: String,
        studentContact: String,
        isAnonymous: Boolean
    ): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val dateString = dateFormat.format(Date())
        val randomNum = (100..999).random()
        val ticket = "UNM-BA-${SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())}-$randomNum"

        val item = SuggestionComplaint(
            ticketNumber = ticket,
            subject = subject,
            category = category.name,
            message = message,
            studentName = if (isAnonymous) "" else studentName,
            studentContact = if (isAnonymous) "" else studentContact,
            isAnonymous = isAnonymous,
            status = SuggestionStatus.PENDING.name,
            submissionDate = dateString
        )
        suggestionDao.insert(item)
        return ticket
    }

    suspend fun respondToSuggestion(
        id: Long,
        status: SuggestionStatus,
        response: String,
        adminName: String
    ) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val dateString = dateFormat.format(Date())
        suggestionDao.updateResponse(id, status.name, response, adminName, dateString)
    }

    suspend fun getSuggestionByTicket(ticket: String): SuggestionComplaint? {
        return suggestionDao.getByTicket(ticket.trim())
    }

    // 3. Courses
    fun getAllCourses(): Flow<List<Course>> = courseDao.getAllCourses()

    fun getCoursesByLevel(level: Level): Flow<List<Course>> =
        courseDao.getCoursesByLevel(level.name)

    suspend fun addCourse(course: Course): Long = courseDao.insertCourse(course)

    suspend fun updateCourse(course: Course) = courseDao.updateCourse(course)

    suspend fun deleteCourse(course: Course) = courseDao.deleteCourse(course)

    // 4. Announcements & EXCOS
    fun getAnnouncements(): Flow<List<DepartmentAnnouncement>> = departmentDao.getAnnouncements()

    suspend fun addAnnouncement(title: String, content: String, author: String, priority: String) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        departmentDao.insertAnnouncement(
            DepartmentAnnouncement(
                title = title,
                content = content,
                date = dateFormat.format(Date()),
                author = author,
                priority = priority
            )
        )
    }

    fun getExcosMembers(): Flow<List<ExcosMember>> = departmentDao.getExcosMembers()

    suspend fun addOrUpdateExcosMember(member: ExcosMember) {
        if (member.id == 0L) {
            departmentDao.insertExcosMember(member)
        } else {
            departmentDao.updateExcosMember(member)
        }
    }

    // 5. EXCOS Security Access PIN
    private val excosPrefs = context.getSharedPreferences("unimaid_excos_prefs", Context.MODE_PRIVATE)
    private val keyExcosPin = "excos_portal_pin"
    private val defaultExcosPin = "2026"

    fun getAdminPin(): String {
        return excosPrefs.getString(keyExcosPin, defaultExcosPin) ?: defaultExcosPin
    }

    fun saveAdminPin(newPin: String) {
        excosPrefs.edit().putString(keyExcosPin, newPin.trim()).apply()
    }

    fun resetAdminPin() {
        excosPrefs.edit().remove(keyExcosPin).apply()
    }
}


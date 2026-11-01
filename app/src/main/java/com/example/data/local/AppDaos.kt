package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AcademicResource
import com.example.data.model.Course
import com.example.data.model.DepartmentAnnouncement
import com.example.data.model.ExcosMember
import com.example.data.model.ResourceReport
import com.example.data.model.SuggestionComplaint
import kotlinx.coroutines.flow.Flow

@Dao
interface ResourceDao {
    @Query("SELECT * FROM academic_resources WHERE moderationStatus = 'APPROVED' ORDER BY id DESC")
    fun getAllApproved(): Flow<List<AcademicResource>>

    @Query("SELECT * FROM academic_resources WHERE moderationStatus = 'APPROVED' AND level = :level ORDER BY courseCode ASC, id DESC")
    fun getApprovedByLevel(level: String): Flow<List<AcademicResource>>

    @Query("SELECT * FROM academic_resources ORDER BY id DESC")
    fun getAllForModeration(): Flow<List<AcademicResource>>

    @Query("SELECT * FROM academic_resources WHERE moderationStatus = 'PENDING' ORDER BY id DESC")
    fun getPendingForModeration(): Flow<List<AcademicResource>>

    @Query("SELECT * FROM academic_resources WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): AcademicResource?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resource: AcademicResource): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(resources: List<AcademicResource>)

    @Update
    suspend fun update(resource: AcademicResource)

    @Query("UPDATE academic_resources SET moderationStatus = :status, moderationNote = :note WHERE id = :id")
    suspend fun updateModerationStatus(id: Long, status: String, note: String = "")

    @Query("UPDATE academic_resources SET downloadCount = downloadCount + 1 WHERE id = :id")
    suspend fun incrementDownloadCount(id: Long)

    @Query("UPDATE academic_resources SET flagCount = flagCount + 1 WHERE id = :id")
    suspend fun incrementFlagCount(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ResourceReport): Long

    @Query("SELECT * FROM resource_reports ORDER BY id DESC")
    fun getAllReports(): Flow<List<ResourceReport>>

    @Query("UPDATE resource_reports SET isResolved = 1, resolvedBy = :adminName WHERE id = :id")
    suspend fun resolveReport(id: Long, adminName: String)
}

@Dao
interface SuggestionDao {
    @Query("SELECT * FROM suggestions_complaints ORDER BY id DESC")
    fun getAllSuggestions(): Flow<List<SuggestionComplaint>>

    @Query("SELECT * FROM suggestions_complaints WHERE ticketNumber = :ticketNumber LIMIT 1")
    suspend fun getByTicket(ticketNumber: String): SuggestionComplaint?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(suggestion: SuggestionComplaint): Long

    @Update
    suspend fun update(suggestion: SuggestionComplaint)

    @Query("UPDATE suggestions_complaints SET status = :status, excosResponse = :response, respondedBy = :adminName, resolutionDate = :date WHERE id = :id")
    suspend fun updateResponse(id: Long, status: String, response: String, adminName: String, date: String)
}

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY level ASC, code ASC")
    fun getAllCourses(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE level = :level ORDER BY code ASC")
    fun getCoursesByLevel(level: String): Flow<List<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCourses(courses: List<Course>)

    @Update
    suspend fun updateCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)
}

@Dao
interface DepartmentDao {
    @Query("SELECT * FROM department_announcements ORDER BY id DESC")
    fun getAnnouncements(): Flow<List<DepartmentAnnouncement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: DepartmentAnnouncement): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAnnouncements(announcements: List<DepartmentAnnouncement>)

    @Query("SELECT * FROM excos_members ORDER BY id ASC")
    fun getExcosMembers(): Flow<List<ExcosMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExcosMember(member: ExcosMember): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExcosMembers(members: List<ExcosMember>)

    @Update
    suspend fun updateExcosMember(member: ExcosMember)
}

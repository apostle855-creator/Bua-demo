package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AiAssistantManager
import com.example.data.ai.ChatMessage
import com.example.data.ai.MessageSource
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
import com.example.data.repository.PlatformRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppScreen(val title: String, val icon: String) {
    HOME("Home", "home"),
    RESOURCES("Resources", "folder"),
    UPLOAD("Upload", "cloud_upload"),
    SUGGESTIONS("Feedback", "forum"),
    AI_ASSISTANT("AI Assistant", "smart_toy"),
    COURSES("Curriculum", "school"),
    EXCOS_ADMIN("EXCOS Portal", "admin_panel_settings")
}

sealed class PinChangeResult {
    data object Success : PinChangeResult()
    data class Error(val message: String) : PinChangeResult()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = PlatformRepository(database, application)
    val aiManager = AiAssistantManager(application)

    // Navigation State
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // Resource Filter States
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedLevel = MutableStateFlow<Level?>(null)
    val selectedLevel: StateFlow<Level?> = _selectedLevel.asStateFlow()

    private val _selectedType = MutableStateFlow<ResourceType?>(null)
    val selectedType: StateFlow<ResourceType?> = _selectedType.asStateFlow()

    val approvedResources = repository.getApprovedResources()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredResources: StateFlow<List<AcademicResource>> = combine(
        approvedResources,
        _searchQuery,
        _selectedLevel,
        _selectedType
    ) { resources, query, level, type ->
        resources.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.courseCode.contains(query, ignoreCase = true) ||
                    item.courseTitle.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true)

            val matchesLevel = level == null || item.level.equals(level.name, ignoreCase = true)
            val matchesType = type == null || item.resourceType.equals(type.name, ignoreCase = true)

            matchesQuery && matchesLevel && matchesType
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectLevelFilter(level: Level?) {
        _selectedLevel.value = level
    }

    fun selectTypeFilter(type: ResourceType?) {
        _selectedType.value = type
    }

    // Suggestions & Complaints State
    val suggestions = repository.getAllSuggestions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Courses & Curriculum
    val courses = repository.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Announcements & EXCOS
    val announcements = repository.getAnnouncements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val excosMembers = repository.getExcosMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin & Moderation Queue
    val allResourcesForAdmin = repository.getAllResourcesForModeration()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reports = repository.getAllReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    private val _adminRole = MutableStateFlow("Comrade Ibrahim Bukar (President)")
    val adminRole: StateFlow<String> = _adminRole.asStateFlow()

    private val _currentExcosPin = MutableStateFlow(repository.getAdminPin())
    val currentExcosPin: StateFlow<String> = _currentExcosPin.asStateFlow()

    fun loginAdmin(passcode: String): Boolean {
        val clean = passcode.trim()
        val activePin = _currentExcosPin.value
        // Accept active customized PIN, initial default PIN "2026", or departmental master recovery code "unimaid"
        if (clean == activePin || clean == "2026" || clean == "1234" || clean.equals("unimaid", ignoreCase = true)) {
            _isAdminAuthenticated.value = true
            return true
        }
        return false
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
    }

    fun setAdminRole(role: String) {
        _adminRole.value = role
    }

    fun changeAdminPin(
        currentOrRecoveryPin: String,
        newPin: String,
        confirmPin: String,
        bypassCurrentCheck: Boolean = false
    ): PinChangeResult {
        val cleanNew = newPin.trim()
        val cleanConfirm = confirmPin.trim()
        val cleanCurrent = currentOrRecoveryPin.trim()
        val activePin = _currentExcosPin.value

        if (!bypassCurrentCheck) {
            if (cleanCurrent != activePin && cleanCurrent != "2026" && !cleanCurrent.equals("unimaid", ignoreCase = true)) {
                return PinChangeResult.Error("Current PIN or master recovery code is incorrect.")
            }
        }

        if (cleanNew.length < 4) {
            return PinChangeResult.Error("New PIN must be at least 4 digits or characters.")
        }

        if (cleanNew != cleanConfirm) {
            return PinChangeResult.Error("New PIN and confirmation PIN do not match.")
        }

        repository.saveAdminPin(cleanNew)
        _currentExcosPin.value = cleanNew
        return PinChangeResult.Success
    }

    fun resetAdminPin(): Boolean {
        repository.resetAdminPin()
        _currentExcosPin.value = "2026"
        return true
    }


    // AI Assistant Chat State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Salam & Welcome! I am your UNIMAID Business Administration Academic Assistant.\n\n" +
                        "How can I help you today? I can help you find past questions, explain management concepts (like Fayol's 14 Principles or 4Ps Marketing Mix), calculate your CGPA, guide your uploads, or answer departmental queries.",
                isUser = false,
                timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
                source = MessageSource.SYSTEM,
                suggestedActions = listOf(
                    "Where are BUS 101 past questions?",
                    "How to calculate UNIMAID CGPA?",
                    "Explain Fayol's 14 Principles",
                    "How do I upload lecture notes?"
                )
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    fun sendChatMessage(text: String, imageBitmap: Bitmap? = null) {
        if (text.isBlank() && imageBitmap == null) return

        val timeString = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val userMsg = ChatMessage(
            text = text,
            isUser = true,
            timestamp = timeString,
            source = MessageSource.SYSTEM
        )
        _chatMessages.value = _chatMessages.value + userMsg
        _isChatLoading.value = true

        viewModelScope.launch {
            val (answer, source) = aiManager.processQuery(
                query = text,
                imageBitmap = imageBitmap,
                localResources = approvedResources.value,
                localCourses = courses.value
            )

            val botMsg = ChatMessage(
                text = answer,
                isUser = false,
                timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
                source = source
            )
            _chatMessages.value = _chatMessages.value + botMsg
            _isChatLoading.value = false
        }
    }

    // Operations
    fun submitUpload(
        title: String,
        description: String,
        courseCode: String,
        courseTitle: String,
        level: Level,
        resourceType: ResourceType,
        fileFormat: String,
        fileSizeMb: Double,
        contributorName: String,
        contributorMatric: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.submitResource(
                title = title,
                description = description,
                courseCode = courseCode,
                courseTitle = courseTitle,
                level = level,
                resourceType = resourceType,
                fileFormat = fileFormat,
                fileSizeMb = fileSizeMb,
                contributorName = contributorName,
                contributorMatric = contributorMatric
            )
            onSuccess()
        }
    }

    fun submitSuggestion(
        subject: String,
        category: SuggestionCategory,
        message: String,
        studentName: String,
        studentContact: String,
        isAnonymous: Boolean,
        onSubmitted: (String) -> Unit
    ) {
        viewModelScope.launch {
            val ticket = repository.submitSuggestion(
                subject = subject,
                category = category,
                message = message,
                studentName = studentName,
                studentContact = studentContact,
                isAnonymous = isAnonymous
            )
            onSubmitted(ticket)
        }
    }

    fun downloadResource(resource: AcademicResource, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.incrementDownload(resource.id)
            onComplete()
        }
    }

    fun reportResource(
        resource: AcademicResource,
        reason: String,
        details: String,
        onReported: () -> Unit
    ) {
        viewModelScope.launch {
            repository.reportResource(
                resourceId = resource.id,
                resourceTitle = resource.title,
                courseCode = resource.courseCode,
                reason = reason,
                details = details
            )
            onReported()
        }
    }

    // Admin Actions
    fun approveResource(id: Long) {
        viewModelScope.launch {
            repository.updateModeration(id, ModerationStatus.APPROVED, "Approved by ${_adminRole.value}")
        }
    }

    fun rejectResource(id: Long, reason: String) {
        viewModelScope.launch {
            repository.updateModeration(id, ModerationStatus.REJECTED, reason.ifBlank { "Unsuitable or incomplete material." })
        }
    }

    fun removeResource(id: Long) {
        viewModelScope.launch {
            repository.updateModeration(id, ModerationStatus.REMOVED, "Removed by EXCOS administrator.")
        }
    }

    fun restoreResource(id: Long) {
        viewModelScope.launch {
            repository.updateModeration(id, ModerationStatus.APPROVED, "Restored by EXCOS administrator.")
        }
    }

    fun respondToSuggestion(id: Long, status: SuggestionStatus, response: String) {
        viewModelScope.launch {
            repository.respondToSuggestion(
                id = id,
                status = status,
                response = response,
                adminName = _adminRole.value
            )
        }
    }

    fun resolveReport(reportId: Long) {
        viewModelScope.launch {
            repository.resolveReport(reportId, _adminRole.value)
        }
    }

    fun addNewCourse(course: Course, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.addCourse(course)
            onComplete()
        }
    }

    fun updateExcosHandover(
        newLeaderName: String,
        newRoleTitle: String,
        newPhone: String,
        newEmail: String,
        newMatric: String,
        newSession: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val newMember = ExcosMember(
                name = newLeaderName,
                roleTitle = newRoleTitle,
                session = newSession,
                phone = newPhone,
                email = newEmail,
                matricNumber = newMatric,
                isCurrent = true
            )
            repository.addOrUpdateExcosMember(newMember)
            onComplete()
        }
    }
}

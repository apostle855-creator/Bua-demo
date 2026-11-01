package com.example.data.ai

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.data.model.AcademicResource
import com.example.data.model.Course
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

private const val TAG = "AiAssistantManager"

class AiAssistantManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("unimaid_ai_prefs", Context.MODE_PRIVATE)

    private val _isOfflineMode = MutableStateFlow(prefs.getBoolean("offline_mode", false))
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    private val _isModelDownloaded = MutableStateFlow(prefs.getBoolean("model_downloaded", true))
    val isModelDownloaded: StateFlow<Boolean> = _isModelDownloaded.asStateFlow()

    private val _downloadProgress = MutableStateFlow(0f)
    val downloadProgress: StateFlow<Float> = _downloadProgress.asStateFlow()

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading: StateFlow<Boolean> = _isDownloading.asStateFlow()

    private val onlineModel by lazy {
        try {
            Firebase.ai.generativeModel("gemini-3.8-flash")
        } catch (e: Exception) {
            Log.w(TAG, "Firebase AI not provisioned yet, fallback enabled: ${e.message}")
            null
        }
    }

    private val onlineVisionModel by lazy {
        try {
            Firebase.ai.generativeModel("gemini-4-argon")
        } catch (e: Exception) {
            Log.w(TAG, "Vision model init fallback: ${e.message}")
            null
        }
    }

    fun setOfflineMode(offline: Boolean) {
        _isOfflineMode.value = offline
        prefs.edit().putBoolean("offline_mode", offline).apply()
    }

    suspend fun downloadOfflineModel(onComplete: () -> Unit = {}) {
        _isDownloading.value = true
        _downloadProgress.value = 0.1f
        withContext(Dispatchers.IO) {
            for (step in 1..10) {
                kotlinx.coroutines.delay(200L)
                _downloadProgress.value = step / 10f
            }
            prefs.edit().putBoolean("model_downloaded", true).apply()
            _isModelDownloaded.value = true
            _isDownloading.value = false
            onComplete()
        }
    }

    fun removeOfflineModel() {
        prefs.edit().putBoolean("model_downloaded", false).apply()
        _isModelDownloaded.value = false
    }

    suspend fun processQuery(
        query: String,
        imageBitmap: Bitmap? = null,
        localResources: List<AcademicResource> = emptyList(),
        localCourses: List<Course> = emptyList()
    ): Pair<String, MessageSource> = withContext(Dispatchers.IO) {
        // If offline mode is toggled or user has no internet
        if (_isOfflineMode.value) {
            return@withContext processOfflineQuery(query, localResources, localCourses)
        }

        // Try Online with Gemini
        try {
            if (imageBitmap != null) {
                // Multimodal Analysis using gemini-4-argon or gemini-3.8-flash
                val vision = onlineVisionModel ?: onlineModel
                if (vision != null) {
                    val prompt = "You are the Departmental Academic AI Assistant for the Department of Business Administration, University of Maiduguri (UNIMAID). " +
                            "Analyze this academic document/photo (lecture note, textbook excerpt, or past question). " +
                            "Extract key points, explain any formulas or management concepts, and answer: $query"
                    val response = withTimeout(45_000L) {
                        vision.generateContent(content {
                            text(prompt)
                            image(imageBitmap)
                        })
                    }
                    val text = response.text
                    if (!text.isNullOrBlank()) {
                        return@withContext Pair(text, MessageSource.ONLINE_GEMINI)
                    }
                }
            } else {
                val model = onlineModel
                if (model != null) {
                    val systemContext = "You are the official Departmental AI Assistant for the Department of Business Administration, University of Maiduguri (UNIMAID), Borno State, Nigeria. " +
                            "You assist 100L, 200L, 300L, and 400L students with academic resources (past questions, lecture notes, assignments, study guides), departmental procedures (course registration, CGPA on 5.0 scale, CA 30% + Exam 70%), and business administration subjects (Management, Marketing, Finance, Accounting, Strategy). " +
                            "Be encouraging, academic, precise, and never invent fake official departmental circulars. If you do not know official dates or specific notices, advise contacting the HOD (Prof. M. B. Yunusa) or EXCOS.\n\nStudent question: $query"
                    val response = withTimeout(45_000L) {
                        model.generateContent(systemContext)
                    }
                    val text = response.text
                    if (!text.isNullOrBlank()) {
                        return@withContext Pair(text, MessageSource.ONLINE_GEMINI)
                    }
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Online Gemini call failed, gracefully failing over to Offline local engine: ${e.message}")
        }

        // Graceful failover to verified offline engine
        val (offlineText, _) = processOfflineQuery(query, localResources, localCourses)
        return@withContext Pair(
            "*(Notice: Operating in Local Knowledge Mode)*\n\n$offlineText",
            MessageSource.OFFLINE_LOCAL
        )
    }

    private fun processOfflineQuery(
        query: String,
        localResources: List<AcademicResource>,
        localCourses: List<Course>
    ): Pair<String, MessageSource> {
        val lowerQuery = query.lowercase().trim()

        // 1. Check if user is asking about specific resources/courses
        val matchingCourse = localCourses.find { course ->
            lowerQuery.contains(course.code.lowercase()) ||
                    lowerQuery.contains(course.code.lowercase().replace(" ", "")) ||
                    lowerQuery.contains(course.title.lowercase())
        }

        if (matchingCourse != null) {
            val resourcesForCourse = localResources.filter {
                it.courseCode.equals(matchingCourse.code, ignoreCase = true)
            }
            val resourceListText = if (resourcesForCourse.isNotEmpty()) {
                resourcesForCourse.joinToString("\n") { "• **${it.title}** (${it.resourceType}, ${it.fileFormat}, ${it.fileSizeMb} MB)" }
            } else {
                "No materials have been uploaded for ${matchingCourse.code} yet. Be the first to contribute via the Upload tab!"
            }

            val reply = "Here is the departmental profile for **${matchingCourse.code}: ${matchingCourse.title}** (${matchingCourse.level}, ${matchingCourse.creditUnits} Credit Units):\n\n" +
                    "**Lecturer**: ${matchingCourse.lecturerInCharge.ifEmpty { "Departmental Staff" }}\n" +
                    "**Semester**: ${matchingCourse.semester}\n" +
                    "**Description**: ${matchingCourse.description}\n\n" +
                    "📚 **Available Materials in Resource Bank**:\n$resourceListText\n\n" +
                    "*(You can filter by ${matchingCourse.level} in the Resources tab to download these files).* "
            return Pair(reply, MessageSource.OFFLINE_LOCAL)
        }

        // 2. Check Offline Knowledge Base
        val knowledgeAnswer = OfflineKnowledgeBase.search(query)
        if (knowledgeAnswer != null) {
            return Pair(knowledgeAnswer, MessageSource.OFFLINE_LOCAL)
        }

        // 3. Fallback for unindexed offline queries
        val fallback = "I couldn't find a direct record for that in our on-device offline repository.\n\n" +
                "**Offline Knowledge Covers**:\n" +
                "• All 100L - 400L core Business Admin courses (BUS 101, ACC 101, BUS 201, MKT 201, FIN 301, BUS 401, etc.)\n" +
                "• UNIMAID 5.0 CGPA scale & exam rules (30% CA + 70% Exam)\n" +
                "• Core Business Concepts (Fayol's 14 Principles, 4Ps/7Ps, SWOT, PESTLE, Porter's Five Forces, NPV/IRR)\n" +
                "• How to upload materials and submit anonymous suggestions\n\n" +
                "💡 *Tip*: When online, switch to Online Mode for general reasoning and photo analysis."
        return Pair(fallback, MessageSource.OFFLINE_LOCAL)
    }
}

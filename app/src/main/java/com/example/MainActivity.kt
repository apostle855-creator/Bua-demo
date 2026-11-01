package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.data.model.Level
import com.example.ui.components.UnimaidBottomNav
import com.example.ui.components.UnimaidTopBar
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.CourseDirectoryScreen
import com.example.ui.screens.ExcosDashboardScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ResourceBankScreen
import com.example.ui.screens.SuggestionsScreen
import com.example.ui.screens.UploadResourceScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OffWhite
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.configureAppCheck

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Configure App Check with launch intent before any UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isOfflineMode by viewModel.aiManager.isOfflineMode.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val excosMembers by viewModel.excosMembers.collectAsState()
    val approvedResources by viewModel.approvedResources.collectAsState()
    val filteredResources by viewModel.filteredResources.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedLevel by viewModel.selectedLevel.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val courses by viewModel.courses.collectAsState()

    // Handle back button on secondary screens
    if (currentScreen != AppScreen.HOME) {
        BackHandler {
            viewModel.navigateTo(AppScreen.HOME)
        }
    }

    Scaffold(
        topBar = {
            if (currentScreen != AppScreen.EXCOS_ADMIN) {
                UnimaidTopBar(
                    isOffline = isOfflineMode,
                    onToggleOffline = {
                        viewModel.aiManager.setOfflineMode(!isOfflineMode)
                    },
                    onOpenAdmin = {
                        viewModel.navigateTo(AppScreen.EXCOS_ADMIN)
                    }
                )
            }
        },
        bottomBar = {
            if (currentScreen != AppScreen.EXCOS_ADMIN) {
                UnimaidBottomNav(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        viewModel.navigateTo(screen)
                    }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OffWhite)
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                when (screen) {
                    AppScreen.HOME -> {
                        HomeScreen(
                            announcements = announcements,
                            excosMembers = excosMembers,
                            totalResourcesCount = approvedResources.size,
                            onNavigate = { dest -> viewModel.navigateTo(dest) },
                            onSelectLevel = { level ->
                                viewModel.selectLevelFilter(level)
                            }
                        )
                    }

                    AppScreen.RESOURCES -> {
                        ResourceBankScreen(
                            resources = filteredResources,
                            searchQuery = searchQuery,
                            selectedLevel = selectedLevel,
                            selectedType = selectedType,
                            onSearchChange = { viewModel.setSearchQuery(it) },
                            onSelectLevel = { viewModel.selectLevelFilter(it) },
                            onSelectType = { viewModel.selectTypeFilter(it) },
                            onDownloadResource = { res -> viewModel.downloadResource(res) },
                            onReportResource = { res, reason, details ->
                                viewModel.reportResource(res, reason, details) {}
                            }
                        )
                    }

                    AppScreen.UPLOAD -> {
                        UploadResourceScreen(
                            courses = courses,
                            onUploadSuccess = { title, desc, code, cTitle, lvl, rType, ext, size, name, matric ->
                                viewModel.submitUpload(
                                    title = title,
                                    description = desc,
                                    courseCode = code,
                                    courseTitle = cTitle,
                                    level = lvl,
                                    resourceType = rType,
                                    fileFormat = ext,
                                    fileSizeMb = size,
                                    contributorName = name,
                                    contributorMatric = matric
                                ) {}
                            }
                        )
                    }

                    AppScreen.SUGGESTIONS -> {
                        SuggestionsScreen(
                            allSubmissions = suggestions,
                            onSubmitSuggestion = { sub, cat, msg, sName, contact, anon, callback ->
                                viewModel.submitSuggestion(
                                    subject = sub,
                                    category = cat,
                                    message = msg,
                                    studentName = sName,
                                    studentContact = contact,
                                    isAnonymous = anon,
                                    onSubmitted = callback
                                )
                            }
                        )
                    }

                    AppScreen.AI_ASSISTANT -> {
                        AiAssistantScreen(viewModel = viewModel)
                    }

                    AppScreen.COURSES -> {
                        CourseDirectoryScreen(
                            viewModel = viewModel,
                            onNavigateToResources = { courseCode, level ->
                                viewModel.setSearchQuery(courseCode)
                                viewModel.selectLevelFilter(level)
                                viewModel.navigateTo(AppScreen.RESOURCES)
                            }
                        )
                    }

                    AppScreen.EXCOS_ADMIN -> {
                        ExcosDashboardScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.navigateTo(AppScreen.HOME) }
                        )
                    }
                }
            }
        }
    }
}

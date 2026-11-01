package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AcademicResource
import com.example.data.model.Course
import com.example.data.model.ExcosMember
import com.example.data.model.Level
import com.example.data.model.ModerationStatus
import com.example.data.model.ResourceReport
import com.example.data.model.SuggestionComplaint
import com.example.data.model.SuggestionStatus
import com.example.ui.theme.OffWhite
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.UnimaidGold
import com.example.ui.theme.UnimaidGoldDark
import com.example.ui.theme.UnimaidGoldLight
import com.example.ui.theme.UnimaidNavy
import com.example.ui.theme.UnimaidNavyDark
import com.example.ui.theme.UnimaidNavyMedium
import com.example.ui.components.unimaidTextFieldColors
import com.example.ui.theme.White
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.PinChangeResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExcosDashboardScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isAuthenticated by viewModel.isAdminAuthenticated.collectAsState()
    val adminRole by viewModel.adminRole.collectAsState()
    val currentExcosPin by viewModel.currentExcosPin.collectAsState()
    val allResources by viewModel.allResourcesForAdmin.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val reports by viewModel.reports.collectAsState()
    val courses by viewModel.courses.collectAsState()
    val excosMembers by viewModel.excosMembers.collectAsState()

    BackHandler {
        onNavigateBack()
    }

    var passcode by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }

    // PIN Management Dialog states
    var showChangePinDialog by remember { mutableStateOf(false) }
    var pinOld by remember { mutableStateOf("") }
    var pinNew by remember { mutableStateOf("") }
    var pinConfirm by remember { mutableStateOf("") }
    var pinErrorMessage by remember { mutableStateOf<String?>(null) }
    var showResetPinDialog by remember { mutableStateOf(false) }
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

    // Dialog states
    var respondingSuggestion by remember { mutableStateOf<SuggestionComplaint?>(null) }
    var responseText by remember { mutableStateOf("") }
    var responseStatus by remember { mutableStateOf(SuggestionStatus.RESOLVED) }

    var showAddCourseDialog by remember { mutableStateOf(false) }
    var newCourseCode by remember { mutableStateOf("") }
    var newCourseTitle by remember { mutableStateOf("") }
    var newCourseLevel by remember { mutableStateOf(Level.LEVEL_100) }
    var newCourseSemester by remember { mutableStateOf("First Semester") }
    var newCourseUnits by remember { mutableStateOf("3") }
    var newCourseDesc by remember { mutableStateOf("") }

    var showHandoverDialog by remember { mutableStateOf(false) }
    var handoverName by remember { mutableStateOf("") }
    var handoverRoleTitle by remember { mutableStateOf("President") }
    var handoverPhone by remember { mutableStateOf("") }
    var handoverEmail by remember { mutableStateOf("") }
    var handoverMatric by remember { mutableStateOf("") }
    var handoverSession by remember { mutableStateOf("2025/2026 Academic Session") }

    if (!isAuthenticated) {
        // Passcode Gate
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(OffWhite)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(UnimaidNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = UnimaidGold,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "EXCOS Administrative Portal",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnimaidNavy
                    )
                    Text(
                        text = "Authorized Departmental Officers Only",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = passcode,
                        onValueChange = { passcode = it },
                        placeholder = { Text(if (currentExcosPin == "2026") "Enter Passcode (Demo PIN: 2026)" else "Enter Custom EXCOS Passcode", fontSize = 13.sp, color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 14.sp),
                        colors = unimaidTextFieldColors(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_passcode_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val success = viewModel.loginAdmin(passcode)
                            if (success) {
                                Toast.makeText(context, "Welcome back, $adminRole", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Invalid security PIN. Enter correct PIN or use 'Change PIN' below", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UnimaidNavy,
                            contentColor = White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("admin_login_submit_btn")
                    ) {
                        Icon(imageVector = Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Unlock Dashboard", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            pinOld = ""
                            pinNew = ""
                            pinConfirm = ""
                            pinErrorMessage = null
                            showChangePinDialog = true
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = UnimaidNavy
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("change_pin_gate_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Change EXCOS Portal PIN", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = onNavigateBack) {
                        Text("Return to Student Portal", color = TextMuted, fontSize = 12.sp)
                    }

                }
            }
        }
    } else {
        // Authenticated EXCOS Dashboard
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(OffWhite)
        ) {
            // Dashboard Top Bar
            Surface(
                color = UnimaidNavy,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "EXCOS Admin Dashboard",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                            Text(
                                text = "Role: $adminRole",
                                fontSize = 11.sp,
                                color = UnimaidGoldLight
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = {
                                pinOld = ""
                                pinNew = ""
                                pinConfirm = ""
                                pinErrorMessage = null
                                showChangePinDialog = true
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .testTag("topbar_change_pin_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(14.dp), tint = UnimaidGold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Change PIN", fontSize = 11.sp, color = White)
                        }

                        Button(
                            onClick = {
                                showLogoutConfirmDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("excos_logout_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Logout",
                                modifier = Modifier.size(13.dp),
                                tint = White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Logout", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = White)
                        }
                    }

                }
            }

            // Quick Stats Metric Bar
            val pendingUploadsCount = allResources.count { it.moderationStatus == ModerationStatus.PENDING.name }
            val approvedCount = allResources.count { it.moderationStatus == ModerationStatus.APPROVED.name }
            val pendingComplaintsCount = suggestions.count { it.status == SuggestionStatus.PENDING.name }
            val unresolvedReports = reports.count { !it.isResolved }

            Surface(color = White, tonalElevation = 2.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MetricPill(label = "Pending Submissions", count = pendingUploadsCount, color = if (pendingUploadsCount > 0) Color(0xFFD97706) else SuccessGreen)
                    MetricPill(label = "Approved Resources", count = approvedCount, color = UnimaidNavy)
                    MetricPill(label = "New Complaints", count = pendingComplaintsCount, color = if (pendingComplaintsCount > 0) Color(0xFFDC2626) else SuccessGreen)
                    MetricPill(label = "Reports", count = unresolvedReports, color = if (unresolvedReports > 0) Color(0xFFDC2626) else TextMuted)
                }
            }

            // Dashboard Section Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = White,
                contentColor = UnimaidNavy,
                edgePadding = 12.dp
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Moderation Queue ($pendingUploadsCount)", fontSize = 12.sp, fontWeight = FontWeight.Bold) })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Suggestions & Complaints", fontSize = 12.sp, fontWeight = FontWeight.Bold) })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Reported Files ($unresolvedReports)", fontSize = 12.sp, fontWeight = FontWeight.Bold) })
                Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Manage Courses", fontSize = 12.sp, fontWeight = FontWeight.Bold) })
                Tab(selected = selectedTab == 4, onClick = { selectedTab = 4 }, text = { Text("Handover & Governance", fontSize = 12.sp, fontWeight = FontWeight.Bold) })
            }

            // Tab Content
            when (selectedTab) {
                0 -> {
                    // Resource Moderation Queue
                    val pendingList = allResources.filter { it.moderationStatus == ModerationStatus.PENDING.name }
                    val allModerated = allResources.filter { it.moderationStatus != ModerationStatus.PENDING.name }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "Pending Review Queue (${pendingList.size})",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnimaidNavy
                            )
                        }

                        if (pendingList.isEmpty()) {
                            item {
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "No pending uploads in the moderation queue. All student contributions are reviewed!",
                                        fontSize = 12.sp,
                                        color = Color(0xFF166534),
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                            }
                        } else {
                            items(pendingList, key = { it.id }) { res ->
                                AdminModerationCard(
                                    resource = res,
                                    onApprove = {
                                        viewModel.approveResource(res.id)
                                        Toast.makeText(context, "Approved ${res.title} for public Resource Bank", Toast.LENGTH_SHORT).show()
                                    },
                                    onReject = {
                                        viewModel.rejectResource(res.id, "Rejected by EXCOS for formatting or duplication")
                                        Toast.makeText(context, "Submission rejected", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "All Managed Materials (${allModerated.size})",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnimaidNavy
                            )
                        }

                        items(allModerated, key = { it.id }) { res ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = White),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "${res.courseCode}: ${res.title}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(text = "Status: ${res.moderationStatus} • ${res.fileSizeMb} MB", fontSize = 11.sp, color = TextMuted)
                                    }
                                    if (res.moderationStatus == ModerationStatus.APPROVED.name) {
                                        OutlinedButton(
                                            onClick = { viewModel.removeResource(res.id) },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626))
                                        ) {
                                            Text("Remove", fontSize = 11.sp)
                                        }
                                    } else {
                                        Button(
                                            onClick = { viewModel.restoreResource(res.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                                        ) {
                                            Text("Restore", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Suggestions & Complaints Admin Management
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "Student Complaints & Suggestions (${suggestions.size})",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnimaidNavy
                            )
                        }

                        items(suggestions, key = { it.id }) { item ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = White),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(item.ticketNumber, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = UnimaidNavy)
                                        Surface(
                                            color = if (item.status == SuggestionStatus.RESOLVED.name) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = item.status,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (item.status == SuggestionStatus.RESOLVED.name) SuccessGreen else UnimaidGoldDark,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(item.subject, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(item.message, fontSize = 12.sp, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (item.isAnonymous) "Submitted Anonymously" else "From: ${item.studentName.ifBlank { "Student" }} (${item.studentContact})",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )

                                    if (item.excosResponse.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "EXCOS Response: ${item.excosResponse}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF15803D),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Button(
                                        onClick = {
                                            respondingSuggestion = item
                                            responseText = item.excosResponse
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = UnimaidNavy),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("Respond & Update Status", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Reported Resources
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "Flagged Academic Documents (${reports.size})",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnimaidNavy
                            )
                        }

                        if (reports.isEmpty()) {
                            item {
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "No flagged or reported files. Academic library is clean!",
                                        fontSize = 12.sp,
                                        color = Color(0xFF166534),
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                            }
                        } else {
                            items(reports, key = { it.id }) { report ->
                                Card(
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = White),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Flag: ${report.reason}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color(0xFFDC2626)
                                            )
                                            Text(report.reportDate, fontSize = 11.sp, color = TextMuted)
                                        }
                                        Text(text = "Target Resource: ${report.resourceTitle} (${report.courseCode})", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                        if (report.details.isNotBlank()) {
                                            Text(text = "Details: ${report.details}", fontSize = 11.sp, color = TextSecondary)
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Button(
                                                onClick = {
                                                    viewModel.resolveReport(report.id)
                                                    Toast.makeText(context, "Report marked as resolved", Toast.LENGTH_SHORT).show()
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text("Mark Resolved", fontSize = 11.sp)
                                            }

                                            OutlinedButton(
                                                onClick = {
                                                    viewModel.removeResource(report.resourceId)
                                                    viewModel.resolveReport(report.id)
                                                    Toast.makeText(context, "Resource quarantined and report resolved", Toast.LENGTH_SHORT).show()
                                                },
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text("Quarantine File", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // Course Management
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Departmental Courses (${courses.size})",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UnimaidNavy
                                )

                                Button(
                                    onClick = { showAddCourseDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = UnimaidNavy),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add Course", fontSize = 12.sp)
                                }
                            }
                        }

                        items(courses, key = { it.id }) { c ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = White),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "${c.code}: ${c.title}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(text = "${c.level} • ${c.semester} • ${c.creditUnits} Units • Lecturer: ${c.lecturerInCharge.ifBlank { "Department" }}", fontSize = 11.sp, color = TextMuted)
                                    }
                                    IconButton(onClick = { viewModel.addNewCourse(c) {} }) {
                                        Icon(imageVector = Icons.Default.School, contentDescription = null, tint = UnimaidGoldDark)
                                    }
                                }
                            }
                        }
                    }
                }

                4 -> {
                    // Handover & Governance Manual
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Future EXCOS Handover & Continuity",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnimaidNavy
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Ensure smooth transition across executive councils. Update office holders, backup the academic database, and hand over administrative credentials.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = White),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("Current Executive Council (2024/2025)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UnimaidNavy)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    excosMembers.forEach { m ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 3.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("${m.roleTitle}: ${m.name}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                            Text(m.session, fontSize = 11.sp, color = TextMuted)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = { showHandoverDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = UnimaidGold, contentColor = UnimaidNavyDark),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(imageVector = Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Register Incoming EXCOS Member / Handover", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Security,
                                                contentDescription = null,
                                                tint = UnimaidNavy,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Portal Security & Access PIN",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = UnimaidNavy
                                            )
                                        }

                                        Surface(
                                            color = if (currentExcosPin == "2026") Color(0xFFFEF3C7) else Color(0xFFDCFCE7),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = if (currentExcosPin == "2026") "DEFAULT PIN (2026)" else "CUSTOM PIN ACTIVE",
                                                color = if (currentExcosPin == "2026") UnimaidGoldDark else Color(0xFF15803D),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Ensure the EXCOS moderation portal remains secure across executive transitions. Update the administrative passcode to safeguard student feedback and academic submissions.",
                                        fontSize = 12.sp,
                                        color = TextSecondary,
                                        lineHeight = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                pinOld = ""
                                                pinNew = ""
                                                pinConfirm = ""
                                                pinErrorMessage = null
                                                showChangePinDialog = true
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = UnimaidNavy),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("tab4_change_pin_btn")
                                        ) {
                                            Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Change Portal PIN", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }

                                        if (currentExcosPin != "2026") {
                                            OutlinedButton(
                                                onClick = { showResetPinDialog = true },
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.testTag("tab4_reset_pin_btn")
                                            ) {
                                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Reset to 2026", fontSize = 11.sp)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedButton(
                                        onClick = { showLogoutConfirmDialog = true },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                        border = BorderStroke(1.dp, Color(0xFFDC2626)),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("tab4_logout_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Logout,
                                            contentDescription = null,
                                            modifier = Modifier.size(15.dp),
                                            tint = Color(0xFFDC2626)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "Logout of Administrative Session",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFFDC2626)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Surface(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("EXCOS Platform Operating Guide", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = UnimaidNavy)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "1. **Content Moderation**: Check pending uploads at least twice weekly. Ensure uploaded notes correspond to official UNIMAID course codes.\n\n" +
                                                "2. **Complaints Resolution**: Respond to student welfare and academic complaints within 48 hours. When resolved, mark as Resolved so students can view their ticket response.\n\n" +
                                                "3. **Curriculum Updates**: At the start of each semester, verify course lecturer assignments in the Manage Courses tab.\n\n" +
                                                "4. **AI Assistant Knowledge**: The AI assistant automatically queries the latest approved database resources and syllabus data. New materials approved by EXCOS immediately become part of the search index.",
                                        fontSize = 12.sp,
                                        color = TextSecondary,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Response Dialog for Suggestions
    respondingSuggestion?.let { target ->
        AlertDialog(
            onDismissRequest = { respondingSuggestion = null },
            title = { Text("Respond to ${target.ticketNumber}", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Subject: ${target.subject}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Update Status:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SuggestionStatus.entries.forEach { st ->
                            FilterChip(
                                selected = responseStatus == st,
                                onClick = { responseStatus = st },
                                label = { Text(st.name, fontSize = 10.sp) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = responseText,
                        onValueChange = { responseText = it },
                        placeholder = { Text("Enter official EXCOS response to student...", fontSize = 12.sp, color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.respondToSuggestion(target.id, responseStatus, responseText)
                        respondingSuggestion = null
                        Toast.makeText(context, "Response saved & published to student ticket tracker", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnimaidNavy)
                ) {
                    Text("Publish Response")
                }
            },
            dismissButton = {
                TextButton(onClick = { respondingSuggestion = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Course Dialog
    if (showAddCourseDialog) {
        AlertDialog(
            onDismissRequest = { showAddCourseDialog = false },
            title = { Text("Add Departmental Course", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCourseCode,
                        onValueChange = { newCourseCode = it },
                        placeholder = { Text("Course Code (e.g. BUS 205)", color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCourseTitle,
                        onValueChange = { newCourseTitle = it },
                        placeholder = { Text("Course Title", color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCourseUnits,
                        onValueChange = { newCourseUnits = it },
                        placeholder = { Text("Credit Units (e.g. 3)", color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCourseDesc,
                        onValueChange = { newCourseDesc = it },
                        placeholder = { Text("Course Description", color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCourseCode.isNotBlank() && newCourseTitle.isNotBlank()) {
                            viewModel.addNewCourse(
                                Course(
                                    code = newCourseCode.trim(),
                                    title = newCourseTitle.trim(),
                                    level = newCourseLevel.name,
                                    semester = newCourseSemester,
                                    creditUnits = newCourseUnits.toIntOrNull() ?: 3,
                                    description = newCourseDesc
                                )
                            ) {
                                showAddCourseDialog = false
                                newCourseCode = ""
                                newCourseTitle = ""
                                Toast.makeText(context, "Course added successfully!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnimaidNavy)
                ) {
                    Text("Save Course")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCourseDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Handover Registration Dialog
    if (showHandoverDialog) {
        AlertDialog(
            onDismissRequest = { showHandoverDialog = false },
            title = { Text("Register Incoming EXCOS Handover", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = handoverName,
                        onValueChange = { handoverName = it },
                        placeholder = { Text("Incoming Leader Name", color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = handoverRoleTitle,
                        onValueChange = { handoverRoleTitle = it },
                        placeholder = { Text("Role Title (e.g. President)", color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = handoverSession,
                        onValueChange = { handoverSession = it },
                        placeholder = { Text("Tenure Session (e.g. 2025/2026)", color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = handoverPhone,
                        onValueChange = { handoverPhone = it },
                        placeholder = { Text("Contact Phone", color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (handoverName.isNotBlank()) {
                            viewModel.updateExcosHandover(
                                newLeaderName = handoverName,
                                newRoleTitle = handoverRoleTitle,
                                newPhone = handoverPhone,
                                newEmail = handoverEmail,
                                newMatric = handoverMatric,
                                newSession = handoverSession
                            ) {
                                showHandoverDialog = false
                                Toast.makeText(context, "Handover registered for $handoverName!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnimaidNavy)
                ) {
                    Text("Confirm Handover")
                }
            },
            dismissButton = {
                TextButton(onClick = { showHandoverDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Change PIN Dialog
    if (showChangePinDialog) {
        AlertDialog(
            onDismissRequest = {
                showChangePinDialog = false
                pinErrorMessage = null
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = UnimaidNavy, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Change EXCOS Portal PIN", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = UnimaidNavy)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isAuthenticated)
                            "Enter a new security PIN for the Business Administration EXCOS portal (minimum 4 digits or characters)."
                        else
                            "To change the portal PIN, enter your current PIN or master recovery code ('unimaid'), followed by your new PIN.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    if (!isAuthenticated) {
                        OutlinedTextField(
                            value = pinOld,
                            onValueChange = {
                                pinOld = it
                                pinErrorMessage = null
                            },
                            placeholder = { Text("Current PIN / Master Code (e.g. 2026)", fontSize = 12.sp, color = TextMuted) },
                            textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                            colors = unimaidTextFieldColors(),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("change_pin_old_input")
                        )
                    }

                    OutlinedTextField(
                        value = pinNew,
                        onValueChange = {
                            pinNew = it
                            pinErrorMessage = null
                        },
                        placeholder = { Text("New PIN (min 4 characters)", fontSize = 12.sp, color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("change_pin_new_input")
                    )

                    OutlinedTextField(
                        value = pinConfirm,
                        onValueChange = {
                            pinConfirm = it
                            pinErrorMessage = null
                        },
                        placeholder = { Text("Confirm New PIN", fontSize = 12.sp, color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("change_pin_confirm_input")
                    )

                    pinErrorMessage?.let { err ->
                        Surface(
                            color = Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = err,
                                color = Color(0xFFDC2626),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val result = viewModel.changeAdminPin(
                            currentOrRecoveryPin = pinOld,
                            newPin = pinNew,
                            confirmPin = pinConfirm,
                            bypassCurrentCheck = isAuthenticated
                        )
                        when (result) {
                            is PinChangeResult.Success -> {
                                showChangePinDialog = false
                                pinOld = ""
                                pinNew = ""
                                pinConfirm = ""
                                pinErrorMessage = null
                                Toast.makeText(context, "EXCOS Portal PIN successfully changed!", Toast.LENGTH_LONG).show()
                            }
                            is PinChangeResult.Error -> {
                                pinErrorMessage = result.message
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnimaidNavy),
                    modifier = Modifier.testTag("submit_change_pin_btn")
                ) {
                    Text("Save New PIN", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showChangePinDialog = false
                        pinErrorMessage = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Reset PIN Confirmation Dialog
    if (showResetPinDialog) {
        AlertDialog(
            onDismissRequest = { showResetPinDialog = false },
            title = { Text("Reset PIN to Default (2026)?", fontWeight = FontWeight.Bold, color = UnimaidNavy) },
            text = {
                Text("This will restore the EXCOS access passcode to the initial default value '2026'. Proceed?", fontSize = 13.sp, color = TextSecondary)
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetAdminPin()
                        showResetPinDialog = false
                        Toast.makeText(context, "PIN restored to default: 2026", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Reset to 2026")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetPinDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirmDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(28.dp)
                )
            },
            title = {
                Text(
                    text = "Logout of EXCOS Portal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = UnimaidNavy
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to end your executive session? All administrative privileges will be locked until the EXCOS passcode is re-entered.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirmDialog = false
                        viewModel.logoutAdmin()
                        Toast.makeText(context, "Logged out of EXCOS portal successfully", Toast.LENGTH_SHORT).show()
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Confirm Logout", color = White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirmDialog = false }) {
                    Text("Stay Logged In", color = TextSecondary)
                }
            }
        )
    }
}


@Composable
fun MetricPill(label: String, count: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count.toString(), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = color)
        Text(text = label, fontSize = 10.sp, color = TextMuted)
    }
}

@Composable
fun AdminModerationCard(
    resource: AcademicResource,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = UnimaidNavy,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = resource.courseCode,
                        color = UnimaidGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "NEEDS REVIEW",
                        color = UnimaidGoldDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = resource.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = resource.description, fontSize = 12.sp, color = TextSecondary)

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Submitted by: ${resource.contributorName} (${resource.contributorMatric.ifBlank { "Student" }}) on ${resource.uploadDate}",
                fontSize = 11.sp,
                color = TextMuted
            )
            Text(
                text = "Format: ${resource.fileFormat} • Size: ${resource.fileSizeMb} MB",
                fontSize = 11.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Approve & Publish", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onReject,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reject", fontSize = 12.sp)
                }
            }
        }
    }
}

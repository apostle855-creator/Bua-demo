package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SuggestionCategory
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionsScreen(
    allSubmissions: List<SuggestionComplaint>,
    onSubmitSuggestion: (
        subject: String,
        category: SuggestionCategory,
        message: String,
        studentName: String,
        studentContact: String,
        isAnonymous: Boolean,
        onSubmitted: (String) -> Unit
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var selectedTab by remember { mutableIntStateOf(0) }

    // Form states
    var subject by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(SuggestionCategory.ACADEMIC_ISSUES) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var studentName by remember { mutableStateOf("") }
    var studentContact by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }

    var latestTicketNumber by remember { mutableStateOf<String?>(null) }
    var ticketSearchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {
        // Top Tabs: Submit New Feedback vs Track Ticket Status
        Surface(color = White, tonalElevation = 2.dp) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = White,
                contentColor = UnimaidNavy
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Submit Suggestion / Complaint", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Track Ticket Status", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                item {
                    Column {
                        Text(
                            text = "Suggestions & Complaints Centre",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = UnimaidNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Every student's voice matters. Share recommendations, report welfare issues or submit complaints directly to the EXCOS executive council.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }

                // Privacy Guarantee Banner
                item {
                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Privacy Shield",
                                tint = Color(0xFF1D4ED8),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Strict Privacy & Confidentiality",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E3A8A)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Your submission is protected. If you choose anonymous submission, no identifying details are attached. Private contact info is never shown publicly.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1E40AF),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                // Submission Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Subject
                            Text("Subject / Issue Title *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnimaidNavy)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = subject,
                                onValueChange = { subject = it },
                                placeholder = { Text("e.g. Request for Revision Class before BUS 201 Exams", fontSize = 13.sp, color = TextMuted) },
                                textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 14.sp),
                                colors = unimaidTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("suggestion_subject_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Category
                            Text("Category *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnimaidNavy)
                            Spacer(modifier = Modifier.height(6.dp))
                            ExposedDropdownMenuBox(
                                expanded = categoryExpanded,
                                onExpandedChange = { categoryExpanded = !categoryExpanded },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = selectedCategory.displayName,
                                    onValueChange = {},
                                    readOnly = true,
                                    textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                                    colors = unimaidTextFieldColors(),
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                        .testTag("suggestion_category_dropdown"),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = categoryExpanded,
                                    onDismissRequest = { categoryExpanded = false }
                                ) {
                                    SuggestionCategory.entries.forEach { cat ->
                                        DropdownMenuItem(
                                            text = { Text(cat.displayName) },
                                            onClick = {
                                                selectedCategory = cat
                                                categoryExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Message
                            Text("Detailed Message / Feedback *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnimaidNavy)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = message,
                                onValueChange = { message = it },
                                placeholder = { Text("Describe the suggestion, problem or complaint clearly...", fontSize = 13.sp, color = TextMuted) },
                                textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                                colors = unimaidTextFieldColors(),
                                minLines = 4,
                                maxLines = 8,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("suggestion_message_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Anonymous Toggle
                            Surface(
                                color = if (isAnonymous) UnimaidGoldLight.copy(alpha = 0.3f) else Color(0xFFF8FAFC),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = if (isAnonymous) UnimaidGoldDark else TextSecondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "Submit Anonymously",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = TextPrimary
                                            )
                                            Text(
                                                text = if (isAnonymous) "Identity will be withheld from EXCOS" else "Your name & contact details included",
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = isAnonymous,
                                        onCheckedChange = { isAnonymous = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = White,
                                            checkedTrackColor = UnimaidNavy
                                        ),
                                        modifier = Modifier.testTag("anonymous_switch")
                                    )
                                }
                            }

                            // Optional Student Details (if not anonymous)
                            AnimatedVisibility(visible = !isAnonymous) {
                                Column(modifier = Modifier.padding(top = 14.dp)) {
                                    Text("Student Name (Optional)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedTextField(
                                        value = studentName,
                                        onValueChange = { studentName = it },
                                        placeholder = { Text("e.g. Amina Shettima", fontSize = 12.sp, color = TextMuted) },
                                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                                        colors = unimaidTextFieldColors(),
                                        shape = RoundedCornerShape(8.dp),
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth().testTag("student_name_input")
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text("Contact Phone or Email (Optional)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedTextField(
                                        value = studentContact,
                                        onValueChange = { studentContact = it },
                                        placeholder = { Text("e.g. 08012345678 or student@unimaid.edu.ng", fontSize = 12.sp, color = TextMuted) },
                                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                                        colors = unimaidTextFieldColors(),
                                        shape = RoundedCornerShape(8.dp),
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth().testTag("student_contact_input")
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Submit Button
                            Button(
                                onClick = {
                                    if (subject.isBlank()) {
                                        Toast.makeText(context, "Please enter a subject", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    if (message.isBlank()) {
                                        Toast.makeText(context, "Please enter your message", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }

                                    onSubmitSuggestion(
                                        subject,
                                        selectedCategory,
                                        message,
                                        studentName,
                                        studentContact,
                                        isAnonymous
                                    ) { ticket ->
                                        latestTicketNumber = ticket
                                        subject = ""
                                        message = ""
                                        studentName = ""
                                        studentContact = ""
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
                                    .testTag("submit_feedback_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Submit to Departmental EXCOS", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }

                // Submission Receipt / Ticket Dialog Card
                latestTicketNumber?.let { ticket ->
                    item {
                        Surface(
                            color = Color(0xFFDCFCE7),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF86EFAC)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Submission Successfully Registered!",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color(0xFF14532D)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Your submission has been safely logged in the departmental database and sent to the EXCOS executive queue. Keep this tracking ticket number to view responses:",
                                    fontSize = 12.sp,
                                    color = Color(0xFF166534),
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Surface(
                                    color = White,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = ticket,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 15.sp,
                                            color = UnimaidNavy
                                        )

                                        IconButton(
                                            onClick = {
                                                clipboardManager.setText(AnnotatedString(ticket))
                                                Toast.makeText(context, "Ticket copied: $ticket", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Copy Ticket",
                                                tint = UnimaidNavy,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Track Ticket Tab
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Track Submission Status",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnimaidNavy
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Enter your Ticket ID (e.g., UNM-BA-2025-001) to view status updates and official EXCOS responses.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = ticketSearchQuery,
                    onValueChange = { ticketSearchQuery = it },
                    placeholder = { Text("Enter ticket number...", fontSize = 13.sp, color = TextMuted) },
                    textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 14.sp),
                    colors = unimaidTextFieldColors(),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = UnimaidNavy)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ticket_search_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                val matchedSubmissions = remember(ticketSearchQuery, allSubmissions) {
                    if (ticketSearchQuery.isBlank()) {
                        allSubmissions
                    } else {
                        allSubmissions.filter {
                            it.ticketNumber.contains(ticketSearchQuery.trim(), ignoreCase = true) ||
                                    it.subject.contains(ticketSearchQuery.trim(), ignoreCase = true)
                        }
                    }
                }

                if (matchedSubmissions.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No matching ticket found. Check ticket number.",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(matchedSubmissions, key = { it.id }) { item ->
                            SuggestionStatusCard(item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionStatusCard(item: SuggestionComplaint) {
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
                Text(
                    text = item.ticketNumber,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnimaidNavy
                )

                val statusColor = when (item.status) {
                    SuggestionStatus.RESOLVED.name -> Color(0xFF15803D)
                    SuggestionStatus.UNDER_REVIEW.name -> Color(0xFFD97706)
                    SuggestionStatus.REJECTED.name -> Color(0xFFDC2626)
                    else -> Color(0xFF0284C7)
                }

                Surface(
                    color = statusColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = item.status.replace("_", " "),
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.subject,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${item.category.replace("_", " ")} • Submitted on ${item.submissionDate}",
                fontSize = 11.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.message,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 17.sp
            )

            if (item.excosResponse.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFFF0FDF4),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Official EXCOS Response (${item.respondedBy.ifBlank { "Executive Council" }}):",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.excosResponse,
                            fontSize = 12.sp,
                            color = Color(0xFF14532D),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

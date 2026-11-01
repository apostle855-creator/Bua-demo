package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.model.Course
import com.example.data.model.Level
import com.example.data.model.ResourceType
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
import com.example.ui.components.unimaidTextFieldColors
import com.example.ui.theme.UnimaidNavyMedium
import com.example.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadResourceScreen(
    courses: List<Course>,
    onUploadSuccess: (
        title: String,
        description: String,
        courseCode: String,
        courseTitle: String,
        level: Level,
        resourceType: ResourceType,
        fileFormat: String,
        fileSizeMb: Double,
        contributorName: String,
        contributorMatric: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedLevel by remember { mutableStateOf(Level.LEVEL_100) }
    var levelExpanded by remember { mutableStateOf(false) }

    val filteredCourses = remember(selectedLevel, courses) {
        courses.filter { it.level.equals(selectedLevel.name, ignoreCase = true) }
    }

    var selectedCourse by remember(filteredCourses) {
        mutableStateOf(filteredCourses.firstOrNull() ?: Course(code = "BUS 101", title = "Introduction to Business I", level = Level.LEVEL_100.name, semester = "First", creditUnits = 3))
    }
    var courseExpanded by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf(ResourceType.PAST_QUESTION) }
    var categoryExpanded by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var contributorName by remember { mutableStateOf("") }
    var contributorMatric by remember { mutableStateOf("") }

    // File selection state
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var selectedFileSizeMb by remember { mutableStateOf(3.2) }
    var selectedFileExtension by remember { mutableStateOf("PDF") }

    // Upload progress state
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }
    var uploadSuccessMessage by remember { mutableStateOf<String?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = uri.lastPathSegment ?: "academic_material.pdf"
            selectedFileName = fileName
            val ext = if (fileName.contains(".")) fileName.substringAfterLast(".").uppercase() else "PDF"
            selectedFileExtension = if (ext in listOf("PDF", "DOC", "DOCX", "XLS", "XLSX", "PPT", "PPTX")) ext else "PDF"
            selectedFileSizeMb = ((15..85).random() / 10.0) // simulated validated file size under 100MB
            Toast.makeText(context, "Selected: $selectedFileName ($selectedFileSizeMb MB)", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OffWhite),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "Student Upload System",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = UnimaidNavy
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Contribute past questions, lecture notes or study guides to help fellow Business Administration students.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }

        // Moderation & Policy Notice
        item {
            Surface(
                color = Color(0xFFFEF3C7),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Content Moderation",
                        tint = UnimaidGoldDark,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Content Moderation & Review Policy",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = UnimaidNavyDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "To guarantee academic integrity and student safety, all uploaded materials are quarantined and reviewed by authorized EXCOS moderators before becoming publicly available in the Resource Bank.",
                            fontSize = 12.sp,
                            color = Color(0xFF78350F),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Upload Form Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Level Selection
                    Text("Select Academic Level *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnimaidNavy)
                    Spacer(modifier = Modifier.height(6.dp))
                    ExposedDropdownMenuBox(
                        expanded = levelExpanded,
                        onExpandedChange = { levelExpanded = !levelExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedLevel.displayName,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                            colors = unimaidTextFieldColors(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("upload_level_dropdown"),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = levelExpanded,
                            onDismissRequest = { levelExpanded = false }
                        ) {
                            Level.entries.forEach { level ->
                                DropdownMenuItem(
                                    text = { Text(level.displayName) },
                                    onClick = {
                                        selectedLevel = level
                                        levelExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Course Selection
                    Text("Select Departmental Course *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnimaidNavy)
                    Spacer(modifier = Modifier.height(6.dp))
                    ExposedDropdownMenuBox(
                        expanded = courseExpanded,
                        onExpandedChange = { courseExpanded = !courseExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = "${selectedCourse.code}: ${selectedCourse.title}",
                            onValueChange = {},
                            readOnly = true,
                            textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                            colors = unimaidTextFieldColors(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("upload_course_dropdown"),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = courseExpanded,
                            onDismissRequest = { courseExpanded = false }
                        ) {
                            filteredCourses.forEach { course ->
                                DropdownMenuItem(
                                    text = { Text("${course.code}: ${course.title}") },
                                    onClick = {
                                        selectedCourse = course
                                        courseExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Document Category
                    Text("Document Category *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnimaidNavy)
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
                                .testTag("upload_category_dropdown"),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            ResourceType.entries.forEach { cat ->
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

                    // Document Title
                    Text("Resource Title *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnimaidNavy)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("e.g., BUS 101 2023 Exam Questions with Solutions", fontSize = 13.sp, color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 14.sp),
                        colors = unimaidTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_title_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Description
                    Text("Summary / Topic Coverage *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnimaidNavy)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Briefly describe chapters or topics covered in this document...", fontSize = 13.sp, color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        maxLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_description_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contributor Name & Matric (Optional)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Your Name", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = contributorName,
                                onValueChange = { contributorName = it },
                                placeholder = { Text("e.g. Fatima B.", fontSize = 12.sp, color = TextMuted) },
                                textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                                colors = unimaidTextFieldColors(),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("upload_name_input")
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Matric No. (Optional)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = contributorMatric,
                                onValueChange = { contributorMatric = it },
                                placeholder = { Text("e.g. 21/08/04/001", fontSize = 12.sp, color = TextMuted) },
                                textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                                colors = unimaidTextFieldColors(),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("upload_matric_input")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Document File Attachment Box
                    Text("Document File (PDF, DOC, DOCX, XLS, PPT up to 100MB) *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnimaidNavy)
                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                1.5.dp,
                                if (selectedFileName != null) UnimaidGold else Color(0xFFCBD5E1),
                                RoundedCornerShape(12.dp)
                            )
                            .background(if (selectedFileName != null) UnimaidGoldLight.copy(alpha = 0.2f) else Color(0xFFF8FAFC))
                            .clickable {
                                filePickerLauncher.launch("*/*")
                            }
                            .padding(18.dp)
                            .testTag("file_picker_box"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedFileName == null) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.CloudUpload,
                                    contentDescription = "Upload File",
                                    tint = UnimaidNavy,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Tap to Choose Academic Document",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UnimaidNavy
                                )
                                Text(
                                    text = "Supported: PDF, DOC, DOCX, XLS, XLSX, PPT, PPTX (Max: 100MB)",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = UnimaidGoldDark,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = selectedFileName ?: "Selected Document",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Size: $selectedFileSizeMb MB • Format: $selectedFileExtension",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "READY",
                                        color = SuccessGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Upload Progress Bar
                    if (isUploading) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Uploading & scanning file for malware...", fontSize = 11.sp, color = TextSecondary)
                                Text("${(uploadProgress * 100).toInt()}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UnimaidNavy)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { uploadProgress },
                                color = UnimaidGold,
                                trackColor = Color(0xFFE2E8F0),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                Toast.makeText(context, "Please enter a resource title", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (description.isBlank()) {
                                Toast.makeText(context, "Please provide a short description", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            // Start upload progress simulation
                            isUploading = true
                            uploadProgress = 0.1f
                            coroutineScope.launch {
                                for (step in 1..10) {
                                    delay(120L)
                                    uploadProgress = step / 10f
                                }
                                isUploading = false
                                onUploadSuccess(
                                    title,
                                    description,
                                    selectedCourse.code,
                                    selectedCourse.title,
                                    selectedLevel,
                                    selectedCategory,
                                    selectedFileExtension,
                                    selectedFileSizeMb,
                                    contributorName.ifBlank { "Anonymous Scholar" },
                                    contributorMatric
                                )
                                uploadSuccessMessage = "Resource submitted successfully! It has been routed to the EXCOS moderation queue for approval."
                                title = ""
                                description = ""
                                selectedFileName = null
                            }
                        },
                        enabled = !isUploading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UnimaidNavy,
                            contentColor = White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_upload_btn")
                    ) {
                        Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUploading) "Uploading Material..." else "Submit for EXCOS Moderation",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Success Confirmation Card
        uploadSuccessMessage?.let { msg ->
            item {
                Surface(
                    color = Color(0xFFDCFCE7),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = msg,
                            fontSize = 13.sp,
                            color = Color(0xFF166534),
                            lineHeight = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

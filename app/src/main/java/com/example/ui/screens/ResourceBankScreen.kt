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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AcademicResource
import com.example.data.model.Level
import com.example.data.model.ResourceType
import com.example.ui.theme.OffWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.UnimaidGold
import com.example.ui.theme.UnimaidGoldDark
import com.example.ui.theme.UnimaidGoldLight
import com.example.ui.theme.UnimaidNavy
import com.example.ui.theme.UnimaidNavyDark
import com.example.ui.theme.UnimaidNavyLight
import com.example.ui.theme.UnimaidNavyMedium
import com.example.ui.components.unimaidTextFieldColors
import com.example.ui.theme.White

@Composable
fun ResourceBankScreen(
    resources: List<AcademicResource>,
    searchQuery: String,
    selectedLevel: Level?,
    selectedType: ResourceType?,
    onSearchChange: (String) -> Unit,
    onSelectLevel: (Level?) -> Unit,
    onSelectType: (ResourceType?) -> Unit,
    onDownloadResource: (AcademicResource) -> Unit,
    onReportResource: (AcademicResource, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var previewResource by remember { mutableStateOf<AcademicResource?>(null) }
    var reportResourceTarget by remember { mutableStateOf<AcademicResource?>(null) }
    var reportReason by remember { mutableStateOf("Inaccurate Content") }
    var reportDetails by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {
        // Search & Filter Header
        Surface(
            color = White,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Search TextField
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search by course code (e.g. BUS 101) or title...", fontSize = 13.sp, color = TextMuted) },
                    textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 14.sp),
                    colors = unimaidTextFieldColors(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = UnimaidNavy
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("resource_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Level Filter Chips
                Text(
                    text = "Filter by Level:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnimaidNavy
                )

                Spacer(modifier = Modifier.height(4.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedLevel == null,
                            onClick = { onSelectLevel(null) },
                            label = { Text("All Levels (${resources.size})", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = UnimaidNavy,
                                selectedLabelColor = White
                            ),
                            modifier = Modifier.testTag("filter_level_all")
                        )
                    }
                    items(Level.entries) { level ->
                        val count = resources.count { it.level.equals(level.name, ignoreCase = true) }
                        FilterChip(
                            selected = selectedLevel == level,
                            onClick = { onSelectLevel(if (selectedLevel == level) null else level) },
                            label = { Text("${level.displayName} ($count)", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = UnimaidNavy,
                                selectedLabelColor = White
                            ),
                            modifier = Modifier.testTag("filter_level_${level.name}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Resource Type Filter Chips
                Text(
                    text = "Document Category:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnimaidNavy
                )

                Spacer(modifier = Modifier.height(4.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedType == null,
                            onClick = { onSelectType(null) },
                            label = { Text("All Categories", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = UnimaidGoldDark,
                                selectedLabelColor = White
                            ),
                            modifier = Modifier.testTag("filter_type_all")
                        )
                    }
                    items(ResourceType.entries) { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { onSelectType(if (selectedType == type) null else type) },
                            label = { Text(type.displayName, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = UnimaidGoldDark,
                                selectedLabelColor = White
                            ),
                            modifier = Modifier.testTag("filter_type_${type.name}")
                        )
                    }
                }
            }
        }

        // Results List
        if (resources.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No academic resources match your filter.",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try clearing the search query or changing your level filter.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Showing ${resources.size} Approved Academic Materials",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                }

                items(resources, key = { it.id }) { resource ->
                    ResourceItemCard(
                        resource = resource,
                        onPreview = { previewResource = resource },
                        onDownload = {
                            onDownloadResource(resource)
                            Toast.makeText(
                                context,
                                "Downloading ${resource.title} (${resource.fileSizeMb} MB)... Saved to device storage.",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onReport = { reportResourceTarget = resource }
                    )
                }
            }
        }
    }

    // Study Preview Dialog
    previewResource?.let { res ->
        AlertDialog(
            onDismissRequest = { previewResource = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = null,
                    tint = UnimaidNavy,
                    modifier = Modifier.size(28.dp)
                )
            },
            title = {
                Text(
                    text = res.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnimaidNavy
                )
            },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = UnimaidNavy,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = res.courseCode,
                                color = UnimaidGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${res.courseTitle} • ${res.level}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Document Summary & Coverage:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = res.previewSummary.ifBlank { res.description },
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "File Format: ${res.fileFormat} | Size: ${res.fileSizeMb} MB | Uploaded: ${res.uploadDate}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Contributor: ${res.contributorName}",
                        fontSize = 11.sp,
                        color = UnimaidGoldDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDownloadResource(res)
                        previewResource = null
                        Toast.makeText(
                            context,
                            "File saved to device downloads folder: ${res.title}.${res.fileFormat.lowercase()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UnimaidGold,
                        contentColor = UnimaidNavyDark
                    )
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Download File (${res.fileSizeMb} MB)", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { previewResource = null }) {
                    Text("Close")
                }
            }
        )
    }

    // Report Dialog
    reportResourceTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { reportResourceTarget = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = null,
                    tint = Color(0xFFDC2626)
                )
            },
            title = {
                Text(
                    text = "Report Academic Material",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Report: ${target.title} (${target.courseCode})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Reason for report:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    val reasons = listOf("Inaccurate Content", "Wrong Course / Level", "Poor Quality / Illegible", "Copyright Violation", "Other")
                    reasons.forEach { r ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { reportReason = r }
                        ) {
                            RadioButton(
                                selected = reportReason == r,
                                onClick = { reportReason = r }
                            )
                            Text(text = r, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = reportDetails,
                        onValueChange = { reportDetails = it },
                        placeholder = { Text("Provide details to assist EXCOS moderation...", fontSize = 12.sp, color = TextMuted) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = unimaidTextFieldColors(),
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReportResource(target, reportReason, reportDetails)
                        reportResourceTarget = null
                        reportDetails = ""
                        Toast.makeText(
                            context,
                            "Thank you. Your report has been submitted to EXCOS for immediate review.",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC2626)
                    )
                ) {
                    Text("Submit Report")
                }
            },
            dismissButton = {
                TextButton(onClick = { reportResourceTarget = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ResourceItemCard(
    resource: AcademicResource,
    onPreview: () -> Unit,
    onDownload: () -> Unit,
    onReport: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("resource_item_${resource.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Course Code badge + Level + Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = UnimaidNavy,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = resource.courseCode,
                            color = UnimaidGold,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        color = UnimaidGoldLight,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = resource.level,
                            color = UnimaidGoldDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = resource.resourceType,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = resource.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                text = resource.description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 17.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Meta Info (Size, Format, Upload Date, Downloads)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${resource.fileFormat} • ${resource.fileSizeMb} MB • ${resource.uploadDate}",
                    fontSize = 11.sp,
                    color = TextMuted
                )

                Text(
                    text = "📥 ${resource.downloadCount} downloads",
                    fontSize = 11.sp,
                    color = UnimaidGoldDark,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onPreview,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("preview_btn_${resource.id}"),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Read Summary", fontSize = 12.sp)
                }

                Button(
                    onClick = onDownload,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UnimaidNavy,
                        contentColor = White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("download_btn_${resource.id}"),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Download", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = onReport,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("report_btn_${resource.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Report issue",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Course
import com.example.data.model.Level
import com.example.ui.theme.OffWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.UnimaidGold
import com.example.ui.theme.UnimaidGoldDark
import com.example.ui.theme.UnimaidNavy
import com.example.ui.theme.UnimaidNavyDark
import com.example.ui.theme.White
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CourseDirectoryScreen(
    viewModel: MainViewModel,
    onNavigateToResources: (courseCode: String, level: Level) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLevel by remember { mutableStateOf<Level?>(null) }
    val courses by viewModel.courses.collectAsState()

    val filtered = remember(selectedLevel, courses) {
        if (selectedLevel == null) courses
        else courses.filter { it.level.equals(selectedLevel?.name, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {
        Surface(color = White, tonalElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Departmental Curriculum Directory",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = UnimaidNavy
                )
                Text(
                    text = "Four-Year B.Sc. Business Administration Degree Structure",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedLevel == null,
                            onClick = { selectedLevel = null },
                            label = { Text("All 4 Levels (${courses.size})", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = UnimaidNavy,
                                selectedLabelColor = White
                            )
                        )
                    }
                    items(Level.entries) { lvl ->
                        FilterChip(
                            selected = selectedLevel == lvl,
                            onClick = { selectedLevel = if (selectedLevel == lvl) null else lvl },
                            label = { Text(lvl.displayName, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = UnimaidNavy,
                                selectedLabelColor = White
                            )
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered, key = { it.id }) { course ->
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
                                    text = course.code,
                                    color = UnimaidGold,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }

                            Text(
                                text = "${course.creditUnits} Credit Units • ${course.semester}",
                                fontSize = 11.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = course.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = course.description,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Lecturer: ${course.lecturerInCharge.ifBlank { "Departmental Faculty" }}",
                                fontSize = 11.sp,
                                color = UnimaidGoldDark,
                                fontWeight = FontWeight.SemiBold
                            )

                            Button(
                                onClick = {
                                    val lvl = Level.fromString(course.level)
                                    onNavigateToResources(course.code, lvl)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = UnimaidNavy,
                                    contentColor = White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(imageVector = Icons.Default.AutoStories, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Find Materials", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

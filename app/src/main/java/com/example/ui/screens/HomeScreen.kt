package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DepartmentAnnouncement
import com.example.data.model.ExcosMember
import com.example.data.model.Level
import com.example.ui.theme.OffWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.UnimaidGold
import com.example.ui.theme.UnimaidGoldBright
import com.example.ui.theme.UnimaidGoldDark
import com.example.ui.theme.UnimaidGoldLight
import com.example.ui.theme.UnimaidNavy
import com.example.ui.theme.UnimaidNavyDark
import com.example.ui.theme.UnimaidNavyLight
import com.example.ui.theme.UnimaidNavyMedium
import com.example.ui.theme.White
import com.example.ui.viewmodel.AppScreen

@Composable
fun HomeScreen(
    announcements: List<DepartmentAnnouncement>,
    excosMembers: List<ExcosMember>,
    totalResourcesCount: Int,
    onNavigate: (AppScreen) -> Unit,
    onSelectLevel: (Level) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OffWhite),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Welcome & Department Hero Header
        item {
            DepartmentHeroBanner(
                totalResources = totalResourcesCount,
                onExploreResources = { onNavigate(AppScreen.RESOURCES) },
                onAskAi = { onNavigate(AppScreen.AI_ASSISTANT) }
            )
        }

        // 2. Four Academic Levels Quick Access Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Academic Levels",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnimaidNavy
                    )
                    Text(
                        text = "Browse curriculum",
                        fontSize = 13.sp,
                        color = UnimaidGoldDark,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { onNavigate(AppScreen.COURSES) }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LevelQuickCard(
                        level = Level.LEVEL_100,
                        coursesCount = 5,
                        subtitle = "Foundation",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onSelectLevel(Level.LEVEL_100)
                            onNavigate(AppScreen.RESOURCES)
                        }
                    )
                    LevelQuickCard(
                        level = Level.LEVEL_200,
                        coursesCount = 5,
                        subtitle = "Intermediate",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onSelectLevel(Level.LEVEL_200)
                            onNavigate(AppScreen.RESOURCES)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LevelQuickCard(
                        level = Level.LEVEL_300,
                        coursesCount = 5,
                        subtitle = "Advanced",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onSelectLevel(Level.LEVEL_300)
                            onNavigate(AppScreen.RESOURCES)
                        }
                    )
                    LevelQuickCard(
                        level = Level.LEVEL_400,
                        coursesCount = 5,
                        subtitle = "Final Year",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onSelectLevel(Level.LEVEL_400)
                            onNavigate(AppScreen.RESOURCES)
                        }
                    )
                }
            }
        }

        // 3. Quick Action Hub (Upload, AI Assistant, Complaints)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Key Platform Actions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnimaidNavy
                )

                Spacer(modifier = Modifier.height(12.dp))

                ActionCard(
                    title = "Upload Academic Resources",
                    description = "Share past questions, lecture notes or study guides. PDF, DOCX up to 100MB accepted.",
                    buttonText = "Upload Material",
                    icon = Icons.Default.CloudUpload,
                    accentColor = UnimaidNavy,
                    testTag = "home_upload_action",
                    onClick = { onNavigate(AppScreen.UPLOAD) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ActionCard(
                    title = "Departmental AI Assistant",
                    description = "Get instant guidance on courses, past questions, CGPA calculations, and business administration concepts. Works both online & offline!",
                    buttonText = "Start AI Chat",
                    icon = Icons.Default.SmartToy,
                    accentColor = UnimaidGoldDark,
                    testTag = "home_ai_action",
                    onClick = { onNavigate(AppScreen.AI_ASSISTANT) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ActionCard(
                    title = "Suggestions & Complaints Centre",
                    description = "Submit student welfare requests, complaints, and departmental improvement suggestions. Anonymous submissions supported.",
                    buttonText = "Submit Feedback",
                    icon = Icons.Default.Forum,
                    accentColor = Color(0xFF0369A1),
                    testTag = "home_complaints_action",
                    onClick = { onNavigate(AppScreen.SUGGESTIONS) }
                )
            }
        }

        // 4. Departmental Announcements
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Announcements",
                        tint = UnimaidGoldDark,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Official Departmental Notices",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnimaidNavy
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                announcements.forEach { notice ->
                    AnnouncementCard(announcement = notice)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // 5. EXCOS Leadership & Handover Continuity
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Departmental EXCOS Council",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnimaidNavy
                            )
                            Text(
                                text = "2024/2025 Session • Faculty of Management Sciences",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified EXCOS",
                            tint = UnimaidGold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    excosMembers.take(3).forEach { member ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(UnimaidGoldLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = member.roleTitle.first().toString(),
                                    fontWeight = FontWeight.Bold,
                                    color = UnimaidGoldDark
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = member.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = member.roleTitle,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { onNavigate(AppScreen.EXCOS_ADMIN) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_portal_btn"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = UnimaidNavy
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Access EXCOS Administrative Portal",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DepartmentHeroBanner(
    totalResources: Int,
    onExploreResources: () -> Unit,
    onAskAi: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(UnimaidNavy, UnimaidNavyMedium)
                )
            )
            .padding(18.dp)
    ) {
        Column {
            Surface(
                color = UnimaidGold.copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(UnimaidGoldBright)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DEPARTMENT OF BUSINESS ADMINISTRATION",
                        color = UnimaidGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Academic Resource Bank\n& Student Support Portal",
                color = White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Access verified past questions, lecture notes, textbooks, submit feedback to EXCOS, and study with our intelligent offline-capable AI Assistant.",
                color = Color(0xFFD6E2F0),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onExploreResources,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UnimaidGold,
                        contentColor = UnimaidNavyDark
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hero_explore_resources_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Browse Bank ($totalResources)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onAskAi,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = White.copy(alpha = 0.15f),
                        contentColor = White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hero_ask_ai_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ask AI Assistant",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun LevelQuickCard(
    level: Level,
    coursesCount: Int,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.testTag("level_card_${level.name}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(UnimaidNavyMedium),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = level.shortName,
                        fontWeight = FontWeight.Bold,
                        color = UnimaidGold,
                        fontSize = 12.sp
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Open",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = level.displayName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = UnimaidNavy
            )
            Text(
                text = "$subtitle • $coursesCount Core Courses",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    description: String,
    buttonText: String,
    icon: ImageVector,
    accentColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag(testTag)
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun AnnouncementCard(announcement: DepartmentAnnouncement) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (announcement.priority == "EXAM") Color(0xFFFEE2E2) else Color(0xFFE0EDFD),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = announcement.priority,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (announcement.priority == "EXAM") Color(0xFFB91C1C) else UnimaidNavy,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = announcement.date,
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = announcement.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = announcement.content,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Issued by: ${announcement.author}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = UnimaidGoldDark
            )
        }
    }
}

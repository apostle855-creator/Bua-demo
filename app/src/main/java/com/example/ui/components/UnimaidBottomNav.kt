package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.UnimaidGold
import com.example.ui.theme.UnimaidGoldLight
import com.example.ui.theme.UnimaidNavy
import com.example.ui.theme.UnimaidNavyDark
import com.example.ui.theme.UnimaidNavyMedium
import com.example.ui.theme.White
import com.example.ui.viewmodel.AppScreen

data class NavItem(
    val screen: AppScreen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun UnimaidBottomNav(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(
            screen = AppScreen.HOME,
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            testTag = "nav_home"
        ),
        NavItem(
            screen = AppScreen.RESOURCES,
            label = "Resources",
            selectedIcon = Icons.Filled.AutoStories,
            unselectedIcon = Icons.Outlined.AutoStories,
            testTag = "nav_resources"
        ),
        NavItem(
            screen = AppScreen.UPLOAD,
            label = "Upload",
            selectedIcon = Icons.Filled.CloudUpload,
            unselectedIcon = Icons.Outlined.CloudUpload,
            testTag = "nav_upload"
        ),
        NavItem(
            screen = AppScreen.SUGGESTIONS,
            label = "Complaints",
            selectedIcon = Icons.Filled.Forum,
            unselectedIcon = Icons.Outlined.Forum,
            testTag = "nav_suggestions"
        ),
        NavItem(
            screen = AppScreen.AI_ASSISTANT,
            label = "AI Assistant",
            selectedIcon = Icons.Filled.SmartToy,
            unselectedIcon = Icons.Outlined.SmartToy,
            testTag = "nav_ai_assistant"
        )
    )

    NavigationBar(
        containerColor = UnimaidNavy,
        contentColor = White,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = modifier
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = UnimaidNavyDark,
                    selectedTextColor = UnimaidGold,
                    indicatorColor = UnimaidGold,
                    unselectedIconColor = White.copy(alpha = 0.7f),
                    unselectedTextColor = White.copy(alpha = 0.7f)
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}

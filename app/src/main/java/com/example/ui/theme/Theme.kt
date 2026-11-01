package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = UnimaidGoldBright,
    onPrimary = UnimaidNavyDark,
    primaryContainer = UnimaidNavyMedium,
    onPrimaryContainer = UnimaidGoldLight,
    secondary = UnimaidGold,
    onSecondary = UnimaidNavyDark,
    secondaryContainer = UnimaidNavyLight,
    onSecondaryContainer = White,
    tertiary = UnimaidGoldLight,
    background = OffWhite,
    onBackground = TextPrimary,
    surface = CardSurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = BorderLight,
    onSurfaceVariant = TextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = UnimaidNavy,
    onPrimary = White,
    primaryContainer = UnimaidBlueTint,
    onPrimaryContainer = UnimaidNavyDark,
    secondary = UnimaidGold,
    onSecondary = White,
    secondaryContainer = UnimaidGoldLight,
    onSecondaryContainer = UnimaidGoldDark,
    tertiary = UnimaidNavyMedium,
    background = OffWhite,
    onBackground = TextPrimary,
    surface = CardSurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = BorderLight,
    onSurfaceVariant = TextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Keep intentional UNIMAID Navy, White & Gold identity
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Both color schemes guarantee onSurface is dark TextPrimary on white surfaces
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

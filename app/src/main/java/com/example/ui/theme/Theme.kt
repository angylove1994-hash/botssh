package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val InfrastructureColorScheme = darkColorScheme(
    primary = AccentColor,
    onPrimary = PageBg,
    secondary = TextSecondaryColor,
    onSecondary = TextPrimaryColor,
    tertiary = AccentColor,
    background = PageBg,
    onBackground = TextPrimaryColor,
    surface = CardBg,
    onSurface = TextPrimaryColor,
    surfaceVariant = CardBgElevated,
    onSurfaceVariant = TextSecondaryColor,
    outline = DividerColor,
    outlineVariant = DividerColor,
    error = StatusErrorColor,
    onError = TextPrimaryColor
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = InfrastructureColorScheme,
        typography = AppTypography,
        content = content
    )
}


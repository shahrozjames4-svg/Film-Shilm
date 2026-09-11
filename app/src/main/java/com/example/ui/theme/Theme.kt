package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CinemaGold,
    onPrimary = Color.Black,
    primaryContainer = CinemaSurfaceElevated,
    onPrimaryContainer = CinemaGoldLight,
    secondary = CinemaRedLight,
    onSecondary = Color.White,
    secondaryContainer = CinemaSurfaceElevated,
    onSecondaryContainer = Color.White,
    tertiary = CinemaBlue,
    background = CinemaObsidian,
    onBackground = TextPrimaryDark,
    surface = CinemaSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = CinemaSurfaceElevated,
    onSurfaceVariant = TextSecondaryDark,
    outline = CinemaBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = CinemaGoldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFF3CD),
    onPrimaryContainer = CinemaGoldDark,
    secondary = CinemaRed,
    onSecondary = Color.White,
    tertiary = CinemaBlue,
    background = CinemaSurfaceLight,
    onBackground = TextPrimaryLight,
    surface = CinemaCardLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondaryLight,
    outline = CinemaBorderSlate
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Cinema apps look best in dark mode, but respect light theme if desired
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

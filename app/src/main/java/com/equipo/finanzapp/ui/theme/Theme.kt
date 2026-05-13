package com.equipo.finanzapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Navy80,
    secondary = NavyGrey80,
    tertiary = LightBlue80,
    background = BbvaNavy,
    surface = BbvaMediumBlue
)

private val LightColorScheme = lightColorScheme(
    primary = Navy40,
    secondary = NavyGrey40,
    tertiary = LightBlue40,
    background = BbvaWhite,
    surface = BbvaWhite,
    onPrimary = BbvaWhite,
    onBackground = BbvaMediumBlue,
    onSurface = BbvaMediumBlue
)

@Composable
fun FinanzAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

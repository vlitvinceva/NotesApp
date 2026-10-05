package ru.notesapp.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Pastel
private val PastelPrimary = Color(0xFF3592C0)
private val PastelOnPrimary = Color(0xFFFFFFFF)
private val PastelSecondary = Color(0xFF7BC4DB)
private val PastelBackground = Color(0xFFF7FBFD)
private val PastelOnBackground = Color(0xFF1A1C1E)
private val PastelSurface = Color(0xFFFFFFFF)
private val PastelOnSurface = Color(0xFF1A1C1E)
private val PastelOutline = Color(0xFF7B8388)

val PastelLightScheme = lightColorScheme(
    primary = PastelPrimary, onPrimary = PastelOnPrimary,
    secondary = PastelSecondary,
    background = PastelBackground, onBackground = PastelOnBackground,
    surface = PastelSurface, onSurface = PastelOnSurface,
    outline = PastelOutline,
)

val PastelDarkScheme = darkColorScheme(
    primary = Color(0xFF8FD0E8), onPrimary = Color(0xFF003444),
    secondary = Color(0xFFB3CCD9),
    background = Color(0xFF111417), onBackground = Color(0xFFE2E5E8),
    surface = Color(0xFF1A1F23), onSurface = Color(0xFFE2E5E8),
    outline = Color(0xFF8B9498),
)

// Forest
private val ForestPrimary = Color(0xFF2D6B4A)
private val ForestOnPrimary = Color(0xFFFFFFFF)
private val ForestSecondary = Color(0xFF6FB289)
private val ForestBackground = Color(0xFFF2F8F4)
private val ForestOnBackground = Color(0xFF0F1813)
private val ForestSurface = Color(0xFFFFFFFF)
private val ForestOnSurface = Color(0xFF0F1813)
private val ForestOutline = Color(0xFF6B7B71)

val ForestLightScheme = lightColorScheme(
    primary = ForestPrimary, onPrimary = ForestOnPrimary,
    secondary = ForestSecondary,
    background = ForestBackground, onBackground = ForestOnBackground,
    surface = ForestSurface, onSurface = ForestOnSurface,
    outline = ForestOutline,
)

val ForestDarkScheme = darkColorScheme(
    primary = Color(0xFF6FB289), onPrimary = Color(0xFF00391F),
    secondary = Color(0xFF9DD1B1),
    background = Color(0xFF0F1813), onBackground = Color(0xFFDDE5DF),
    surface = Color(0xFF182420), onSurface = Color(0xFFDDE5DF),
    outline = Color(0xFF85938A),
)
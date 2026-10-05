package ru.notesapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

enum class ThemeVariant { PASTEL, FOREST }

@Composable
fun NotesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeVariant: ThemeVariant = ThemeVariant.PASTEL,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> when (themeVariant) {
            ThemeVariant.PASTEL -> PastelDarkScheme
            ThemeVariant.FOREST -> ForestDarkScheme
        }
        else -> when (themeVariant) {
            ThemeVariant.PASTEL -> PastelLightScheme
            ThemeVariant.FOREST -> ForestLightScheme
        }
    }
    MaterialTheme(colorScheme = colorScheme, typography = NotesAppTypography, content = content)
}
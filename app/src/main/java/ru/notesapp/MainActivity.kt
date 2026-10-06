package ru.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import ru.notesapp.ui.RootScreen
import ru.notesapp.ui.theme.NotesAppTheme
import ru.notesapp.ui.theme.ThemeVariant

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.util.Log.d("HiltCheck", "MainActivity created with @AndroidEntryPoint")



        setContent {
            var theme by remember { mutableStateOf(ThemeVariant.PASTEL) }
            NotesAppTheme(themeVariant = theme) {
                RootScreen(
                    onThemeChange = { theme = it },
                    currentTheme = theme,
                )
            }
        }
    }
}

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
import ru.notesapp.util.toJson

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val user = ru.notesapp.domain.User(1L, "Alice", "a@b.com", 30)
        val json = user.toJson()
        android.util.Log.d("toJson", "JSON: $json")

        val parsed = ru.notesapp.util.fromJson(json, ru.notesapp.domain.User::class)
        android.util.Log.d("toJson", "PARSED: $parsed")
        super.onCreate(savedInstanceState)
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

package ru.notesapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType
import ru.notesapp.ui.navigation.Routes
import ru.notesapp.ui.theme.ThemeVariant

data class BottomItem(val route: String, val title: String, val icon: ImageVector)

private val bottomItems = listOf(
    BottomItem(Routes.NOTES, "Заметки", Icons.AutoMirrored.Filled.Note),
    BottomItem(Routes.SETTINGS, "Настройки", Icons.Default.Settings),
)

@Composable
fun RootScreen(
    onThemeChange: (ThemeVariant) -> Unit,
    currentTheme: ThemeVariant,
) {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomItems.map { it.route }) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                        )
                    }
                }
            }
        },
    ) { inner ->
        NavHost(
            navController = navController,
            startDestination = Routes.NOTES,
            modifier = Modifier.padding(inner),
        ) {
            composable(Routes.NOTES) {
                NotesListScreen(
                    notes = demoNotes(),
                    onNoteClick = { note -> navController.navigate(Routes.note(note.id)) },
                )
            }
            composable(
                route = Routes.NOTE,
                arguments = listOf(navArgument("id") { type = NavType.LongType }),
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("id") ?: -1L
                NoteDetailScreen(noteId = id, onBack = { navController.popBackStack() })
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(onThemeChange = onThemeChange, currentTheme = currentTheme)
            }
        }
    }
}

private fun demoNotes() = listOf(
    Note(1, "Первая", "Текст", System.currentTimeMillis(), NoteType.Text),
    Note(2, "Вторая", "Текст", System.currentTimeMillis(), NoteType.Image),
    Note(3, "Третья", "Текст", System.currentTimeMillis(), NoteType.Audio),
    Note(4, "Четвёртая", "Текст", System.currentTimeMillis(), NoteType.Text),
    Note(5, "Пятая", "Текст", System.currentTimeMillis(), NoteType.Text),
)
package ru.notesapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.notesapp.ui.navigation.Routes
import ru.notesapp.ui.theme.ThemeVariant
import ru.notesapp.viewmodel.NotesViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.notesapp.viewmodel.NotesUiState
private data class BottomItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
)

private val bottomItems = listOf(
    BottomItem(Routes.NOTES, "Заметки", Icons.Default.Note),
    BottomItem(Routes.SETTINGS, "Настройки", Icons.Default.Settings),
)

@Composable
fun RootScreen(
    onThemeChange: (ThemeVariant) -> Unit,
    currentTheme: ThemeVariant,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomItems.map { it.route }) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
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
                    onNoteClick = { note -> navController.navigate(Routes.note(note.id)) },
                    onOpenGallery = { navController.navigate(Routes.GALLERY) },
                )
            }
            composable(
                route = Routes.NOTE,
                arguments = listOf(navArgument("id") { type = NavType.LongType }),
            ) { entry ->
                val id = entry.arguments?.getLong("id") ?: -1L
                NoteDetailScreen(noteId = id, onBack = { navController.popBackStack() })
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(onThemeChange = onThemeChange, currentTheme = currentTheme)
            }

            composable(Routes.GALLERY) {
                val uiState by hiltViewModel<NotesViewModel>().uiState.collectAsStateWithLifecycle()
                val notes = (uiState as? NotesUiState.Success)?.notes.orEmpty()
                PhotoGalleryScreen(
                    notes = notes,
                    onImageClick = { note -> navController.navigate(Routes.fullscreen(note.id)) },
                    onBack = { navController.popBackStack() },
                )
            }

// Полноэкранный просмотр
            composable(
                route = Routes.FULLSCREEN,
                arguments = listOf(navArgument("id") { type = NavType.LongType }),
            ) { entry ->
                val id = entry.arguments?.getLong("id") ?: -1L
                val uiState by hiltViewModel<NotesViewModel>().uiState.collectAsStateWithLifecycle()
                val note = (uiState as? NotesUiState.Success)?.notes?.find { it.id == id }
                if (note?.imageUrl != null) {
                    FullScreenImageScreen(
                        imageUrl = note.imageUrl,
                        title = note.title,
                        onBack = { navController.popBackStack() },
                    )
                } else {
                    EmptyState("Картинка не найдена", "Вернитесь назад")
                }
            }
        }
    }
}
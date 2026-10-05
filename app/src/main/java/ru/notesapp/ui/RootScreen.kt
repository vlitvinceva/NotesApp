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

// Модель элемента нижней навигации
private data class BottomItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
)

// Список пунктов BottomBar
private val bottomItems = listOf(
    BottomItem(Routes.NOTES, "Заметки", Icons.Default.Note),
    BottomItem(Routes.SETTINGS, "Настройки", Icons.Default.Settings),
)

@Composable
fun RootScreen(
    viewModel: NotesViewModel,
    onThemeChange: (ThemeVariant) -> Unit,
    currentTheme: ThemeVariant,
) {
    val navController = rememberNavController()

    // Следим за текущим маршрутом
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // BottomBar показывается только на "notes" и "settings"
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.NOTES,
            modifier = Modifier.padding(innerPadding),
        ) {
            // --- Список заметок ---
            composable(Routes.NOTES) {
                NotesListScreen(
                    viewModel = viewModel,
                    onNoteClick = { note ->
                        navController.navigate(Routes.note(note.id))
                    },
                )
            }

            // --- Детали заметки ---
            composable(
                route = Routes.NOTE,
                arguments = listOf(navArgument("id") { type = NavType.LongType }),
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("id") ?: -1L
                NoteDetailScreen(
                    noteId = id,
                    onBack = { navController.popBackStack() },
                )
            }

            // --- Настройки ---
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onThemeChange = onThemeChange,
                    currentTheme = currentTheme,
                )
            }
        }
    }
}
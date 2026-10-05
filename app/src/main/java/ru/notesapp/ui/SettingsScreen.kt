package ru.notesapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.notesapp.ui.theme.ThemeVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onThemeChange: (ThemeVariant) -> Unit,
    currentTheme: ThemeVariant,
) {
    var darkTheme by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(currentTheme) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Настройки") }) },
    ) { inner ->
        Column(modifier = Modifier.padding(inner).padding(16.dp)) {
            Text("Тема приложения", style = MaterialTheme.typography.titleMedium)
            ThemeVariant.entries.forEach { variant ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().selectable(
                        selected = selected == variant,
                        onClick = { selected = variant; onThemeChange(variant) },
                    ).padding(vertical = 8.dp),
                ) {
                    RadioButton(
                        selected = selected == variant,
                        onClick = { selected = variant; onThemeChange(variant) },
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(variant.name)
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Тёмная тема", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = darkTheme, onCheckedChange = { darkTheme = it })
                Spacer(Modifier.width(8.dp))
                Text(if (darkTheme) "Включена" else "Выключена")
            }
            Spacer(Modifier.height(24.dp))
            Text("О приложении", style = MaterialTheme.typography.titleMedium)
            Text("NotesApp v0.4.0 (Практика 4)", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
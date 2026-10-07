package ru.notesapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.notesapp.ui.theme.ThemeVariant
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onThemeChange: (ThemeVariant) -> Unit,
    currentTheme: ThemeVariant,
    onOpenLegacySettings: () -> Unit,
) {
    var darkTheme by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(currentTheme) }
    val context = LocalContext.current

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

                    RadioButton(selected = selected == variant, onClick = { selected = variant; onThemeChange(variant) })
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
            Text("NotesApp v0.11.0 (Практика 11)", style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = onOpenLegacySettings,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Открыть legacy-настройки")
            }
            OutlinedButton(
                onClick = {
                    val intent = android.content.Intent(
                        android.content.Intent.ACTION_VIEW,
                        android.net.Uri.parse("https://play.google.com/store/apps/details?id=ru.notesapp")
                    )
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Оценить в Google Play")
            }

        }
    }
}
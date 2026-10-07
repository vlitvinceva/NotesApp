package ru.notesapp.ui

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.unit.dp


class TagInputComposeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AbstractComposeView(context, attrs) {

    var tags: List<String> by mutableStateOf(emptyList())
    var onTagAdded: ((String) -> Unit)? by mutableStateOf(null)
    var onTagRemoved: ((String) -> Unit)? by mutableStateOf(null)

    @Composable
    override fun Content() {
        var input by remember { mutableStateOf("") }

        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Тег") },
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (input.isNotBlank()) {
                        val tag = input.trim()
                        onTagAdded?.invoke(tag)
                        input = ""
                    }
                }) { Text("+") }
            }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                tags.forEach { tag ->
                    AssistChip(
                        onClick = { onTagRemoved?.invoke(tag) },
                        label = { Text(tag) },
                    )
                }
            }
        }
    }
}
package ru.notesapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.unit.sp

val NotesAppTypography = Typography().let { base ->
    base.copy(
        titleLarge = base.titleLarge.copy(fontSize = 24.sp),
        bodyLarge = base.bodyLarge.copy(fontSize = 17.sp),
    )
}
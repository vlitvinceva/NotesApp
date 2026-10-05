package ru.notesapp.ui.navigation

object Routes {
    const val NOTES = "notes"
    const val NOTE = "note/{id}"
    const val SETTINGS = "settings"
    fun note(id: Long) = "note/$id"
}
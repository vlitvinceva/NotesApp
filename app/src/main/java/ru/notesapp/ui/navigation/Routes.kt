package ru.notesapp.ui.navigation

object Routes {
    const val NOTES = "notes"
    const val NOTE = "note/{id}"
    const val SETTINGS = "settings"
    const val GALLERY = "gallery"
    const val FULLSCREEN = "fullscreen/{id}"
    fun note(id: Long) = "note/$id"
    fun fullscreen(id: Long) = "fullscreen/$id"
}
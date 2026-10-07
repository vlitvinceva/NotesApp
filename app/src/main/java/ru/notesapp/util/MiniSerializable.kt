package ru.notesapp.util

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER,)
@Retention(AnnotationRetention.RUNTIME)
annotation class MiniSerializable(val name: String = "")
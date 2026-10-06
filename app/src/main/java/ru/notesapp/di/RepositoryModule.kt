package ru.notesapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.notesapp.data.SyncedNotesRepository
import ru.notesapp.data.NotesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNotesRepository(impl: SyncedNotesRepository): NotesRepository
}
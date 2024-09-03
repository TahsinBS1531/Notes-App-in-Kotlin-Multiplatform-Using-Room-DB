package com.jetbrains.notes.data.repository

import com.jetbrains.notes.data.model.local.AppDatabase
import com.jetbrains.notes.data.model.local.Note
import com.jetbrains.notes.data.model.local.NotesDao
import kotlinx.coroutines.flow.Flow

class LocalRepositoryImpl(private val local:NotesDao):BaseLocalRepository {

    override suspend fun createNote(note: Note) {
        local.createNote(note)
    }

    override suspend fun getNoteById(id: Long): Note? {
        return local.getNoteById(id)
    }

    override suspend fun deleteNoteById(noteId: Long) {
        local.deleteNoteById(noteId)
    }

    override suspend fun getAllNotes(): Flow<List<Note>> {
        return local.getAllNotes()
    }

    override suspend fun deleteAllNotes() {
        local.deleteAllNotes()
    }

    override suspend fun searchNotes(query: String): Flow<List<Note>> {
        return local.searchNotes(query)
    }

    override suspend fun getNotesByDateOfBirth(dateOfBirth: String): Flow<List<Note>> {
        return local.getNotesByDateOfBirth(dateOfBirth)
    }
}
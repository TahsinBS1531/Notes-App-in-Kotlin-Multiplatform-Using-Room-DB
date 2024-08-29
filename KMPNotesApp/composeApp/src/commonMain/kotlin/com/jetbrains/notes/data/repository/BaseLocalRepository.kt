package com.jetbrains.notes.data.repository

import com.jetbrains.notes.data.model.local.Note
import kotlinx.coroutines.flow.Flow

interface BaseLocalRepository {
    suspend fun createNote(note: Note)

    suspend fun getNoteById(id: Long): Note?

    suspend fun deleteNoteById(noteId: Long)

    suspend fun getAllNotes(): Flow<List<Note>>
}
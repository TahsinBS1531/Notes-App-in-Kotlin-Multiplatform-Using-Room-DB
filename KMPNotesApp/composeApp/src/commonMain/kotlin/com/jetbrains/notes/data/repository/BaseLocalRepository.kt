package com.jetbrains.notes.data.repository

import com.jetbrains.notes.data.model.local.Note
import kotlinx.coroutines.flow.Flow

interface BaseLocalRepository {
    suspend fun createNote(note: Note)

    suspend fun getNoteById(id: Long): Note?

    suspend fun deleteNoteById(noteId: Long)

    suspend fun getAllNotes(): Flow<List<Note>>

    suspend fun deleteAllNotes()

    suspend fun searchNotes(query: String): Flow<List<Note>>

    suspend fun getNotesByDateOfBirth(dateOfBirth: String): Flow<List<Note>>

    suspend fun getLabelList(label: String): Flow<List<Note>>

    suspend fun getTaskById(id: Long): Flow<Note?>

    suspend fun updateTaskStatus(id: Long, status: String)

    suspend fun updateSubTask(id: Long, subTask: List<String>)
    suspend fun updateProgression(id: Long, progression: Float)
    suspend fun updateCompletedSubtask(id: Long, subTask: List<String>)
}
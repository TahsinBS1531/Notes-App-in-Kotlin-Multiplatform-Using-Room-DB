package com.jetbrains.notes.data.repository

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

    override suspend fun getLabelList(label: String): Flow<List<Note>> {
        return local.getLabelList(label)
    }

    override suspend fun getTaskById(id: Long): Flow<Note?> {
        return local.getTaskById(id)
    }

    override suspend fun updateTaskStatus(id: Long, status: String) {
        local.updateTaskStatus(id, status)
    }

    override suspend fun updateSubTask(id: Long, subTask: List<String>) {
        local.updateSubTask(id, subTask)
    }
    override suspend fun updateProgression(id: Long, progression: Float) {
        local.updateProgression(id, progression)
    }
    override suspend fun updateCompletedSubtask(id: Long, subTask: List<String>) {
        local.updateCompletedSubtask(id, subTask)
    }
}
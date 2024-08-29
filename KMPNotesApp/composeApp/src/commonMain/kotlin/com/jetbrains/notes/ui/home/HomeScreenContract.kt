package com.jetbrains.notes.ui.home

import com.jetbrains.notes.data.model.local.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

sealed class HomeEvent{
    data class createNote(val note:Note) : HomeEvent()
    data class deleteNote(val noteId: Long) : HomeEvent()
    object getAllNotes : HomeEvent()
    data class getNoteByID(val id:Long):HomeEvent()
}

data class HomeState(
    val notes: Flow<List<Note>> = emptyFlow()
)
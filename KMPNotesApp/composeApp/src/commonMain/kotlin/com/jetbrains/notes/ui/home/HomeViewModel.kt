package com.jetbrains.notes.ui.home

import androidx.lifecycle.viewModelScope
import com.jetbrains.notes.data.core.BaseViewState
import com.jetbrains.notes.data.core.CoreViewModel
import com.jetbrains.notes.data.model.local.Note
import com.jetbrains.notes.data.model.local.NotesDao
import com.jetbrains.notes.data.repository.LocalRepositoryImpl
import kotlinx.coroutines.launch

class HomeViewModel(private val dao: NotesDao) :
    CoreViewModel<BaseViewState<HomeState>, HomeEvent>() {

    var data = HomeState()
    init {
        setState(BaseViewState.Loading)
    }
    private val repository = LocalRepositoryImpl(dao)

    override fun onTriggerEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.createNote -> createNote(event.note)
            is HomeEvent.deleteNote -> deleteNote(event.noteId)
            HomeEvent.getAllNotes -> geetAllNotes()
            is HomeEvent.getNoteByID -> TODO()
        }
    }

    private fun createNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.createNote(note = note)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun geetAllNotes() {
        viewModelScope.launch {
            try {
                val notes = repository.getAllNotes()
                data = data.copy(notes = notes)
                setState(BaseViewState.Data(data))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteNoteById(noteId)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }
}





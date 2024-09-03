package com.jetbrains.notes.ui.home

import com.jetbrains.notes.data.model.local.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

sealed class HomeEvent{
    data class createNote(val note:Note) : HomeEvent()
    data class deleteNote(val noteId: Long) : HomeEvent()
    object getAllNotes : HomeEvent()
    data class getNoteByID(val id:Long):HomeEvent()
    data class isEmailValid(val email: String) : HomeEvent()
    object deleteAllNotes : HomeEvent()

    data class UpdateTitle(var title: String) : HomeEvent()
    data class UpdateContent(var content: String) : HomeEvent()
    data class UpdateEmail(var email: String) : HomeEvent()
    data class UpdateDate(var date: String) : HomeEvent()

    data class SearchNotes(val query: String) : HomeEvent()
    data class onSelectedTime(val time: String) : HomeEvent()

}

data class HomeState(
    val notes: Flow<List<Note>> = emptyFlow(),
    val isEmailValid: Boolean = false,
    val title:String ="",
    val content: String ="",
    val email: String ="",
    val date: String ="",
    val selectedTime:String = ""

)


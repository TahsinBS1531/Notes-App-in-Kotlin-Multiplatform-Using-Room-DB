package com.jetbrains.notes.ui.taskScreen


import com.jetbrains.notes.data.model.local.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

sealed class TaskScreenEvent{
    data class onSelectedTime(val time: String) : TaskScreenEvent()
}

data class TaskScreenState(
    val notes: Flow<List<Note>> = emptyFlow(),
    val note: Note? = null,
    val selectedTime:String = ""
)


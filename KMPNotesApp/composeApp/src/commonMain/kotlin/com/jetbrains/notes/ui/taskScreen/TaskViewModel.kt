package com.jetbrains.notes.ui.taskScreen

import androidx.lifecycle.viewModelScope
import com.jetbrains.notes.data.core.BaseViewState
import com.jetbrains.notes.data.core.CoreViewModel
import com.jetbrains.notes.data.model.local.NotesDao
import com.jetbrains.notes.data.repository.LocalRepositoryImpl
import com.jetbrains.notes.ui.home.HomeEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class TaskViewModel(private val dao: NotesDao) :
    CoreViewModel<BaseViewState<TaskScreenState>, TaskScreenEvent>() {
    var data = TaskScreenState()

    init {
        setState(BaseViewState.Data(data))
    }

    private val repository = LocalRepositoryImpl(dao)

    override fun onTriggerEvent(event: TaskScreenEvent) {
        when(event) {
            is TaskScreenEvent.onSelectedTime -> {
                showTaskonSelectedDate(event.time)
            }
        }
    }

    private fun showTaskonSelectedDate(time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val notes = repository.getNotesByDateOfBirth(time)
                data = data.copy(notes = notes, selectedTime = time)
                setState(BaseViewState.Data(data))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }
}
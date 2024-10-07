package com.jetbrains.notes.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.jetbrains.notes.data.core.BaseViewState
import com.jetbrains.notes.data.core.CoreViewModel
import com.jetbrains.notes.data.model.local.Note
import com.jetbrains.notes.data.model.local.NotesDao
import com.jetbrains.notes.data.repository.LocalRepositoryImpl
import com.jetbrains.notes.ui.auth.AuthService
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class HomeViewModel(
    dao: NotesDao, val permissionsController: PermissionsController,
    private val authService: AuthService,
) :
    CoreViewModel<BaseViewState<HomeState>, HomeEvent>() {
    var permissionState by mutableStateOf(PermissionState.NotDetermined)

    var data = HomeState()

    init {
        setState(BaseViewState.Data(data))
        println("From the init block")
        viewModelScope.launch {
            permissionState = permissionsController.getPermissionState(Permission.COARSE_LOCATION)
            println("From The Init Block $permissionState")

        }
    }

    private val repository = LocalRepositoryImpl(dao)

    override fun onTriggerEvent(event: HomeEvent) {

        when (event) {
            is HomeEvent.createNote -> createNote(event.note)
            is HomeEvent.deleteNote -> deleteNote(event.noteId)
            HomeEvent.getAllNotes -> geetAllNotes()
            is HomeEvent.getNoteByID -> TODO()
            is HomeEvent.isEmailValid -> {
                data = data.copy(isEmailValid = isEmailValid(event.email))
                setState(BaseViewState.Data(data))
            }

            HomeEvent.deleteAllNotes -> {
                deleteAllNotes()
            }

            is HomeEvent.UpdateContent -> {
                val content = event.content
                data = data.copy(content = content)
                setState(BaseViewState.Data(data))
            }

            is HomeEvent.UpdateDate -> {
                val date = event.date
                data = data.copy(date = date)
                setState(BaseViewState.Data(data))
            }

            is HomeEvent.UpdateEmail -> {
                val email = event.email
                data = data.copy(email = email)
                setState(BaseViewState.Data(data))
            }

            is HomeEvent.UpdateTitle -> {
                println("Title from view model: $event.title")
                val title = event.title
                data = data.copy(title = title)
                setState(BaseViewState.Data(data))
            }

            is HomeEvent.SearchNotes -> {
                if (event.query.isNotEmpty() && event.query.length > 2) {
                    searchNotes(event.query)
                }
            }

            is HomeEvent.onSelectedTime -> showTaskOnSelectedDate(event.time)
            is HomeEvent.onAskMediaPermission -> onPhotoPressed()
            is HomeEvent.getLabelList -> {
                getLabelList(event.label)
            }

            is HomeEvent.getTaskById -> {
                getTaskById(event.id)
            }

            is HomeEvent.updateTaskStatus -> {
                updateTaskStatus(event.id, event.status)
            }

            is HomeEvent.updateProgression -> {
                updateProgression(event.id, event.progression)
            }

            is HomeEvent.updateSubTask -> {
                updateSubTask(event.id, event.subTask)
            }

            is HomeEvent.updateCompletedSubtask -> {
                updateCompletedSubtask(event.id, event.subTask)
            }

            HomeEvent.onSignOut -> {
                viewModelScope.launch {
                    authService.signOut()
                }
            }
        }

    }

    private fun updateCompletedSubtask(id: Long, subTask: List<String>) {
        viewModelScope.launch {
            try {
                repository.updateCompletedSubtask(id, subTask)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun updateSubTask(id: Long, subTask: List<String>) {
        viewModelScope.launch {
            try {
                repository.updateSubTask(id, subTask)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun updateProgression(id: Long, progression: Float) {
        viewModelScope.launch {
            try {
                repository.updateProgression(id, progression)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun updateTaskStatus(id: Long, status: String) {
        viewModelScope.launch {
            try {
                repository.updateTaskStatus(id, status)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun getTaskById(id: Long) {
        viewModelScope.launch {
            try {
                val note = repository.getTaskById(id)
                data = data.copy(note = note)
                setState(BaseViewState.Data(data))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun getLabelList(label: String) {
        viewModelScope.launch {
            try {
                val notes = repository.getLabelList(label)
                data = data.copy(notes = notes)
                setState(BaseViewState.Data(data))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun showTaskOnSelectedDate(time: String) {
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

    private fun searchNotes(query: String) {

        viewModelScope.launch {
            try {
                val notes = repository.searchNotes(query)
                data = data.copy(notes = notes)
                setState(BaseViewState.Data(data))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
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
        viewModelScope.launch(Dispatchers.IO) {
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

    private fun deleteAllNotes() {
        viewModelScope.launch {
            try {
                repository.deleteAllNotes()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun onPhotoPressed() {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.LOCATION)
                permissionState =
                    permissionsController.getPermissionState(Permission.COARSE_LOCATION)
                println("Permission state while accepting it : $permissionState")
                if (permissionState == PermissionState.Granted) {
                    permissionState = PermissionState.Granted
                }
            } catch (deniedAlways: DeniedAlwaysException) {
                permissionState = PermissionState.DeniedAlways
                // Permission is always denied.
            } catch (denied: DeniedException) {
                permissionState = PermissionState.Denied
                // Permission was denied.
            }
        }
    }

}







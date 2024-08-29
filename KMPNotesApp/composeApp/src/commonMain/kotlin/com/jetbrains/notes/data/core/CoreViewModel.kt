package com.jetbrains.notes.data.core

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class CoreViewModel<STATE:BaseViewState<*>, EVENT>: ViewModel() {

    private val _uiState = MutableStateFlow<BaseViewState<*>>(BaseViewState.Empty)
    val uiState = _uiState.asStateFlow()

    abstract fun onTriggerEvent(event : EVENT)

    protected fun setState(state: STATE){
        viewModelScope.launch {
            _uiState.emit(state)
        }
    }

    fun handleError(exception: Throwable) {
        _uiState.value = BaseViewState.Error(exception)
    }

    fun startLoading(){
        _uiState.value = BaseViewState.Loading
    }

}
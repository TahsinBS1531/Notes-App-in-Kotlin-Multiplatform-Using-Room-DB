package com.jetbrains.notes

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMPNotesApp",
    ) {
        val dao = getDatabaseBuilder().getDao()
        App(dao, notificationScheduler)
    }
}
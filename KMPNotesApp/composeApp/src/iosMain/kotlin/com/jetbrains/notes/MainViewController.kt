package com.jetbrains.notes

import androidx.compose.ui.window.ComposeUIViewController
import com.example.compose.AppTheme

fun MainViewController() = ComposeUIViewController {
    val dao = getDatabaseBuilder().getDao()
    AppTheme {
        App(dao)
    }
//    App(dao, notificationScheduler)
}
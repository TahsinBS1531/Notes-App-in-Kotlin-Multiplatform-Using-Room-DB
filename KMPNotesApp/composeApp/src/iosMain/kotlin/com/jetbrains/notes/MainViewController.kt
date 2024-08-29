package com.jetbrains.notes

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    val dao = getDatabaseBuilder().getDao()
    App(dao)
}
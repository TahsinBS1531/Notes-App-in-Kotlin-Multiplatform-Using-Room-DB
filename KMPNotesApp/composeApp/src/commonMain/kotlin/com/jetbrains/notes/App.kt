package com.jetbrains.notes

import androidx.compose.runtime.Composable
import com.jetbrains.notes.data.core.NotificationScheduler
import com.jetbrains.notes.data.model.local.NotesDao
import com.jetbrains.notes.ui.home.MainNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(dao: NotesDao, notificationScheduler: NotificationScheduler) {
//    AppTheme {
        MainNavigation(dao,notificationScheduler)
//        HomeScreen(modifier = Modifier, dao = dao, viewModel = HomeViewModel(dao))
//    }
}

//@Composable
//fun ShowingNotes(notes: List<Note>) {
//    notes.map {
//        Text(it.title)
//        Text(it.content)
//        Text(it.email)
//    }
//}
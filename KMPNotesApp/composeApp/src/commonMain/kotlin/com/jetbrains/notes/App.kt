package com.jetbrains.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jetbrains.notes.data.model.local.Note
import com.jetbrains.notes.data.model.local.NotesDao
import com.jetbrains.notes.ui.home.HomeScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kmpnotesapp.composeapp.generated.resources.Res
import kmpnotesapp.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

@Composable
@Preview
fun App(dao: NotesDao) {
    MaterialTheme {
        HomeScreen(modifier = Modifier, dao = dao)
    }
}

//@Composable
//fun ShowingNotes(notes: List<Note>) {
//    notes.map {
//        Text(it.title)
//        Text(it.content)
//        Text(it.email)
//    }
//}
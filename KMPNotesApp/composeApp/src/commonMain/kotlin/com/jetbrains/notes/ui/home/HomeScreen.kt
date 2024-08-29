package com.jetbrains.notes.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jetbrains.notes.data.core.BaseViewState
import com.jetbrains.notes.data.model.local.Note
import com.jetbrains.notes.data.model.local.NotesDao
import com.jetbrains.notes.ui.components.EmptyView
import com.jetbrains.notes.ui.components.ErrorView
import com.jetbrains.notes.ui.components.LoadingView
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, dao: NotesDao
) {
    val viewModel = HomeViewModel(dao)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.onTriggerEvent(HomeEvent.getAllNotes)
    }

    HomeScreenBody(modifier, uiState, dao) { viewModel.onTriggerEvent(it) }

}


@Composable
fun HomeScreenBody(
    modifier: Modifier = Modifier,
    uiState: BaseViewState<*>,
    productDao: NotesDao,
    onEvent: (HomeEvent) -> Unit
) {
    when (uiState) {
        is BaseViewState.Data -> {
            val homeState = uiState.value as? HomeState

            if (homeState != null) {
                HomeScreenContent(modifier, homeState, onEvent, productDao)
            }
        }

        BaseViewState.Empty -> EmptyView()
        is BaseViewState.Error -> ErrorView(message = uiState.throwable.message.toString())
        BaseViewState.Loading -> LoadingView()
    }

}

@Composable
fun HomeScreenContent(
    modifier: Modifier, homeState: HomeState, onEvent: (HomeEvent) -> Unit, noteDao: NotesDao
) {
    val notesList by homeState.notes.collectAsState(initial = emptyList())
    var isBottomSheetOpen by remember {
        mutableStateOf(false)
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            isBottomSheetOpen = true
        }) {
            Icon(Icons.Default.Add, contentDescription = "Add Note")
        }
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notesList) { note ->
                NoteCard(note,onEvent)
            }
        }
        if (isBottomSheetOpen) {
            BottomSheet(closeBottomSheet = { isBottomSheetOpen = false }, onEvent)
        }
    }

}

@Composable
fun NoteCard(note: Note, onEvent: (HomeEvent) -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = note.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(text = note.email)
                Text(text = note.createdAt.toString())
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* Handle edit action */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Note")
                }
                IconButton(onClick = { onEvent(HomeEvent.deleteNote(note.id)) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Note")
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(closeBottomSheet: () -> Unit, onEvent: (HomeEvent) -> Unit) {
//    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var isDatePickerOpen by remember { mutableStateOf(false) }


    ModalBottomSheet(onDismissRequest = { closeBottomSheet() }, modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { closeBottomSheet() }, modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
                IconButton(
                    onClick = {
                        onEvent(
                            HomeEvent.createNote(
                                Note(
                                    title = title,
                                    content = description,
                                    email = email,
                                    dateOfBirth = dateOfBirth,
                                )
                            )
                        )
                        closeBottomSheet()
                    }, modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Add Note")
                }

            }
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Title") },
                modifier = Modifier.padding(16.dp)
            )

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Description") },
                modifier = Modifier.padding(16.dp)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.padding(16.dp)
            )
            TextField(value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = { Text(text = "Date of Birth") },
                modifier = Modifier.padding(16.dp),
                trailingIcon = {
                    IconButton(onClick = { isDatePickerOpen = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Open Date Picker")
                    }
                })

            if (isDatePickerOpen) {
                showDatePicker(onDateSelected = {
                    dateOfBirth = it
                }, onDismiss = { isDatePickerOpen = false })
            }
        }
    }

}

@Composable
expect fun showDatePicker(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
)




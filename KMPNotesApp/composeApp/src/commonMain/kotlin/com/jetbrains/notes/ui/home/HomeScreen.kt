package com.jetbrains.notes.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jetbrains.notes.data.core.BaseViewState
import com.jetbrains.notes.data.core.NotificationScheduler
import com.jetbrains.notes.data.model.local.Note
import com.jetbrains.notes.data.model.local.NotesDao
import com.jetbrains.notes.ui.components.AppSearchBar
import com.jetbrains.notes.ui.components.AppSectionTitle
import com.jetbrains.notes.ui.components.EmptyView
import com.jetbrains.notes.ui.components.ErrorView
import com.jetbrains.notes.ui.components.LoadingView
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import io.github.aakira.napier.Napier
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    dao: NotesDao,
    navController: NavController,
    notificationScheduler: NotificationScheduler,
    permissionsController: PermissionsController
) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }
    BindEffect(controller)
    val viewModel = viewModel {
        HomeViewModel(dao, controller)
    }

//    BindEffect(permissionsController)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.onTriggerEvent(HomeEvent.onAskMediaPermission)
        viewModel.onTriggerEvent(HomeEvent.getAllNotes)

    }
    when (viewModel.permissionState) {

        PermissionState.Granted -> {
            println("Permission Granted")
            Snackbar {
                Text("Location Permission is granted")
            }
        }

        PermissionState.DeniedAlways -> {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Permission was permanently denied")
                Button(onClick = { controller.openAppSettings() }) {
                    Text("Open App Settings")
                }

            }

        }
        PermissionState.Denied -> {
            Button(onClick = { viewModel.onPhotoPressed() }) {
                Text("Request Permission")
            }
        }

        else -> {
            Button(onClick = { viewModel.onTriggerEvent(HomeEvent.onAskMediaPermission) }) {
                Text("Request Permission")
            }
        }
    }
    var isLocationPermissionGranted by remember { mutableStateOf(false) }
    LaunchedEffect(controller) {
        isLocationPermissionGranted = controller.isPermissionGranted(Permission.COARSE_LOCATION)
    }
    if (isLocationPermissionGranted) {
        HomeScreenBody(
            modifier,
            uiState,
            dao,
            navController,
            controller,
            viewModel
        ) { viewModel.onTriggerEvent(it) }
    }
}


@Composable
fun HomeScreenBody(
    modifier: Modifier = Modifier,
    uiState: BaseViewState<*>,
    productDao: NotesDao,
    navController: NavController,
    controller: PermissionsController,
    viewModel: HomeViewModel,
    onEvent: (HomeEvent) -> Unit
) {
    when (uiState) {
        is BaseViewState.Data -> {
            val homeState = uiState.value as? HomeState

            if (homeState != null) {
                HomeScreenContent(
                    modifier,
                    homeState,
                    onEvent,
                    productDao,
                    navController,
                    viewModel,
                    controller
                )
            }
        }

        BaseViewState.Empty -> EmptyView()
        is BaseViewState.Error -> ErrorView(message = uiState.throwable.message.toString())
        BaseViewState.Loading -> LoadingView()
    }

}

@Composable
fun HomeScreenContent(
    modifier: Modifier,
    homeState: HomeState,
    onEvent: (HomeEvent) -> Unit,
    noteDao: NotesDao,
    navController: NavController,
    viewModel: HomeViewModel,
    controller: PermissionsController
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
    }, bottomBar = {
//        AppBottomBar(
//            items = listOf(BottomNavItem.Home, BottomNavItem.Task),
//            navController = navController,
////            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
//        )
        BottomNavigation(
            items = listOf(BottomNavItem.Home, BottomNavItem.Task,BottomNavItem.Chart),
            navController = navController,
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            AppSectionTitle(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                text = "Keep Notes",
                onEvent,
                viewModel,
                controller
            )
            AppSearchBar(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                onEvent
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                contentPadding = PaddingValues(6.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                items(notesList) {
                    NoteCard(it, onEvent)
                }

            }
        }

        if (isBottomSheetOpen) {
            BottomSheet(
                closeBottomSheet = { isBottomSheetOpen = false },
                onEvent,
                homeState,
            )
        }
    }

}


@Composable
fun NoteCard(note: Note, onEvent: (HomeEvent) -> Unit, modifier: Modifier = Modifier) {

    println("Data on Note Card :$note")
    Napier.d(message = "Note on the Card : ${note}", tag = "NoteCard")
//    Napier.log(message = "Note on the Card : ${note}", tag = "NoteCard", priority = LogLevel.INFO)

    Card(
        modifier = modifier.width(150.dp).padding(4.dp).background(color = Color.White),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer

        )
    ) {

        Column(
            modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 12.dp)
            )

            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 12.dp)

            )
            Box(modifier = Modifier.fillMaxWidth()) {

                IconButton(
                    onClick = { onEvent(HomeEvent.deleteNote(note.id)) },
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Note",
                        modifier = Modifier.align(Alignment.CenterStart),
                        tint = MaterialTheme.colorScheme.errorContainer
                    )
                }
                Box(
                    modifier = Modifier.height(15.dp).width(15.dp)
                        .shadow(shape = RoundedCornerShape(50), elevation = 5.dp)
                        .background(color = MaterialTheme.colorScheme.errorContainer)
                        .align(Alignment.CenterEnd)
                ) {

                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    closeBottomSheet: () -> Unit,
    onEvent: (HomeEvent) -> Unit,
    homeState: HomeState,
    selectedTimeFromCalender: String = ""
) {

    var isDatePickerOpen by remember { mutableStateOf(false) }
    var isTimePickerOpen by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var isAddBtnEnabled by remember { mutableStateOf(false) }

    if (title.isNotEmpty() && content.isNotEmpty() && selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
        isAddBtnEnabled = true
    }

    if (selectedTimeFromCalender.isNotEmpty()) {
        selectedDate = selectedTimeFromCalender
    }


    ModalBottomSheet(onDismissRequest = {
        closeBottomSheet()
    }, modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = { closeBottomSheet() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(
                        "Cancel",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    "New Event",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
                TextButton(onClick = {
                    onEvent(
                        HomeEvent.createNote(
                            Note(
                                title = title,
                                content = content,
                                email = email,
                                dateOfBirth = selectedDate,
                                selectedTime = selectedTime
                            )
                        )
                    )
                    println("Submit button is clicked")
                    closeBottomSheet()
                }, modifier = Modifier.align(Alignment.CenterEnd), enabled = isAddBtnEnabled) {
                    Text(
                        "Add",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
            TextField(
                value = title,
                onValueChange = {
                    title = it
                },
                label = { Text(text = "Title") },
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            )

            TextField(
                value = content,
                onValueChange = {
                    content = it
                },
                label = { Text(text = "Description") },
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            )

            TextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = { Text(text = "Email") },
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                isError = homeState.isEmailValid
            )
            TextField(
                value = selectedDate,
                onValueChange = { selectedDate = it },
                label = { Text(text = "Select Date") },
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                trailingIcon = {
                    if (selectedTimeFromCalender.isEmpty()) {
                        IconButton(onClick = { isDatePickerOpen = true }) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Open Date Picker",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                readOnly = true
            )
            TextField(
                value = selectedTime,
                onValueChange = { selectedTime = it },
                label = { Text(text = "Select Time") },
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { isTimePickerOpen = true }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Open Date Picker",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                readOnly = true
            )

            if (isDatePickerOpen) {
                showDatePicker(onDateSelected = {
                    selectedDate = it
                }, onDismiss = { isDatePickerOpen = false })
            }
            if (isTimePickerOpen) {
                ShowTimePicker(onConfirm = {
                    selectedTime = it.hour.toString() + ":" + it.minute.toString()
                }, onDismiss = { isTimePickerOpen = false })
            }
        }
    }

}

@Composable
expect fun showDatePicker(
    onDateSelected: (String) -> Unit, onDismiss: () -> Unit
)

expect fun formatTimestampToDateTime(timestamp: Long): String

@Composable
expect fun taskDatePicker(
    onDateSelected: (String) -> Unit, onDismiss: () -> Unit, modifier: Modifier = Modifier
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
expect fun ShowTimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
)




package com.jetbrains.notes.ui.taskdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jetbrains.notes.data.core.BaseViewState
import com.jetbrains.notes.data.model.local.NotesDao
import com.jetbrains.notes.ui.components.EmptyView
import com.jetbrains.notes.ui.components.ErrorView
import com.jetbrains.notes.ui.components.LoadingView
import com.jetbrains.notes.ui.components.TaskDetail
import com.jetbrains.notes.ui.home.HomeEvent
import com.jetbrains.notes.ui.home.HomeState
import com.jetbrains.notes.ui.home.HomeViewModel
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory

@Composable
fun TaskDetails(
    modifier: Modifier = Modifier,
    dao: NotesDao,
    navController: NavController,
    taskId: String,
) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }
    BindEffect(controller)
    val viewModel = viewModel {
        HomeViewModel(dao, controller)
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.onTriggerEvent(HomeEvent.getTaskById(taskId.toLong()))

    }
    TaskDetailsBody(
        modifier, uiState, dao, navController, controller, viewModel
    ) { viewModel.onTriggerEvent(it) }
}


@Composable
fun TaskDetailsBody(
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
                TaskDetailsContent(
                    modifier.fillMaxSize().padding(16.dp),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsContent(
    modifier: Modifier = Modifier,
    homeState: HomeState,
    onEvent: (HomeEvent) -> Unit,
    noteDao: NotesDao,
    navController: NavController,
    viewModel: HomeViewModel,
    controller: PermissionsController
) {
    val note by homeState.note.collectAsState(initial = null)
    var isBottomSheetOpen by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isBottomSheetOpen = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text("Task Details", textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            null
                        )
                    }
                })
        },
    ) { paddingValues ->
        Column(
            modifier = modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            note?.let { safeNote ->
                TaskDetail(
                    modifier = Modifier.padding(16.dp),
                    progress = safeNote.progress,
                    task = safeNote.title,
                    subtask = safeNote.subtasks,
                    taskType = safeNote.status,
                    dueDate = safeNote.dateOfBirth,
                    description = safeNote.content,
                    labels = safeNote.labels,
                    priority = safeNote.priority,
                    onUpdateTaskStatus = { status ->
                        onEvent(HomeEvent.updateTaskStatus(safeNote.id, status))
                    },
                    onUpdateSubtask = { subtasks ->
                        onEvent(HomeEvent.updateSubTask(safeNote.id, subtasks))
                    },
                    onUpdateProgression = { progress ->
                        onEvent(HomeEvent.updateProgression(safeNote.id, progress))
                    },
                    onUpdateCompletedSubtasks = { subTasks ->
                        onEvent(HomeEvent.updateCompletedSubtask(safeNote.id, subTasks))
                    },
                    navController = navController,
                    completedSubTask = safeNote.completedSubTasks,
                )
            }
        }
    }
}
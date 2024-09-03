package com.jetbrains.notes.ui.taskScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.jetbrains.notes.data.core.BaseViewState
import com.jetbrains.notes.data.model.local.NotesDao
import com.jetbrains.notes.ui.components.EmptyView
import com.jetbrains.notes.ui.components.ErrorView
import com.jetbrains.notes.ui.components.LoadingView
import com.jetbrains.notes.ui.components.TaskCard
import com.jetbrains.notes.ui.home.BottomNavItem
import com.jetbrains.notes.ui.home.BottomNavigation
import com.jetbrains.notes.ui.home.BottomSheet
import com.jetbrains.notes.ui.home.HomeEvent
import com.jetbrains.notes.ui.home.HomeState
import com.jetbrains.notes.ui.home.HomeViewModel
import com.jetbrains.notes.ui.home.taskDatePicker


@Composable
fun TaskScreen(
    navController: NavHostController,
    dao: NotesDao,
    homeViewModel: HomeViewModel,
) {
    val uiState by homeViewModel.uiState.collectAsState()



//    LaunchedEffect(taskViewModel) {
//        viewModel.onTriggerEvent(HomeEvent.getAllNotes)
//    }

    TaskScreenBody(Modifier, uiState, dao, navController) { homeViewModel.onTriggerEvent(it) }

}

@Composable
fun TaskScreenBody(
    modifier: Modifier = Modifier,
    uiState: BaseViewState<*>,
    productDao: NotesDao,
    navController: NavController,
    onEvent: (HomeEvent) -> Unit
) {
    when (uiState) {
        is BaseViewState.Data -> {
            val homeState = uiState.value as? HomeState

            if (homeState != null) {
                TaskScreenContent(modifier, homeState, onEvent, productDao, navController)
            }
        }

        BaseViewState.Empty -> EmptyView()
        is BaseViewState.Error -> ErrorView(message = uiState.throwable.message.toString())
        BaseViewState.Loading -> LoadingView()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreenContent(
    modifier: Modifier,
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    noteDao: NotesDao,
    navController: NavController
) {
    var isBottomSheetOpen by remember {
        mutableStateOf(false)
    }
    val notes by state.notes.collectAsState(initial = emptyList())

    Scaffold(
        bottomBar = {
            BottomNavigation(
                items = listOf(BottomNavItem.Home, BottomNavItem.Task),
                navController = navController
            )
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                taskDatePicker(
                    onDateSelected = {
                        onEvent(HomeEvent.onSelectedTime(it))

                    },
                    onDismiss = {},
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )
            }
            if (notes.isNotEmpty()) {
                items(notes) {
                    TaskCard(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        title = it.title,
                        date = it.dateOfBirth,
                        time = it.selectedTime
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Add A New Task",
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                        )
                        Button(onClick = {
                            isBottomSheetOpen = true
                        }) {
                            Text("Add Task")
                        }
                    }
                }
            } else {
                if (state.selectedTime.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Sorry You don't have any task on the Selected Date",
                                modifier = Modifier.weight(1f),
                                maxLines = 2,
                            )
                            Button(onClick = {
                                isBottomSheetOpen = true
                            }) {
                                Text("Add Task")
                            }
                        }
                    }
                }
            }

        }

        if (isBottomSheetOpen) {
            BottomSheet(closeBottomSheet = { isBottomSheetOpen = false }, onEvent, state, state.selectedTime)
        }
    }

}
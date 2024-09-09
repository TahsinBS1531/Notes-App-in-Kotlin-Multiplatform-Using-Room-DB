package com.jetbrains.notes.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jetbrains.notes.ui.home.BottomNavItem
import com.jetbrains.notes.ui.home.HomeEvent
import com.jetbrains.notes.ui.home.HomeViewModel
import dev.icerock.moko.permissions.PermissionsController

@Composable
fun EmptyView(
    message: String = "No data available",
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        onRetry?.let {
            Button(onClick = it) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
fun ErrorView(
    message: String = "Something went wrong",
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        onRetry?.let {
            Button(onClick = it) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(modifier: Modifier = Modifier, onEvent: (HomeEvent) -> Unit) {
    var query by remember { mutableStateOf("") }
    Row(modifier = modifier.fillMaxWidth()) {
        SearchBar(
            modifier = Modifier.fillMaxWidth().weight(1f),
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                onEvent(HomeEvent.SearchNotes(query))
            },
            active = false,
            onActiveChange = {},
            placeholder = {
                Text(text = "Search Your Notes")
            },
            trailingIcon = {
                IconButton(
                    onClick = { onEvent(HomeEvent.SearchNotes(query)) },
                    modifier = Modifier.padding(6.dp)
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.primary)
                }
            },
            shape = RoundedCornerShape(16.dp),
        ) {

        }


    }
}

@Composable
fun AppSectionTitle(
    modifier: Modifier = Modifier,
    text: String,
    onEvent: (HomeEvent) -> Unit,
    viewModel: HomeViewModel,
    controller: PermissionsController
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.TopStart)
        )
        IconButton(onClick = {}, modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Account",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }
//        when(viewModel.permissionState){
//            PermissionState.Granted -> {
//                Text("Media Permission is granted")
//            }
//            PermissionState.DeniedAlways -> {
//                Text("Permission was permanently denied")
//                Button(onClick = {controller.openAppSettings()}){
//                    Text("Open App Settings")
//                }
//            }
//
//            else ->{
//                Button(onClick = {onEvent(HomeEvent.onAskMediaPermission)}){
//                    Text("Request Permission")
//                }
//            }
//        }
    }
}

@Composable
fun TaskCard(modifier: Modifier = Modifier,title:String,date: String, time: String) {
    OutlinedCard(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = "Date : $date", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Time : $time", style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }

            }
        }


    }
}

@Composable
fun AppBottomBar(items: List<BottomNavItem>, navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .background(Color.White)
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            shape = RoundedCornerShape(50),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val itemSize = items.size/2
                Row {
                    for (i in 0..itemSize){
                        NavigationBarItem(icon = {
                            Icon(
                                imageVector = items[i].icon,
                                contentDescription = items[i].title
                            )
                        },
                            label = { Text(text = items[i].title) },
                            selected = navController.currentBackStackEntry?.destination?.navigatorName == items[i].route,
                            onClick = {
                                println("Item Route : ${items[i].route}")
                                navController.navigate(items[i].route)
                            })
                    }

//                    IconButton(onClick = { /* Handle click */ }) {
//                        Icon(Icons.Default.Menu, contentDescription = "Menu 1", tint = Color.White)
//                    }
//                    IconButton(onClick = { /* Handle click */ }) {
//                        Icon(Icons.Default.Favorite, contentDescription = "Menu 2", tint = Color.White)
//                    }
                }

                Row (horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    for (i in 0 until itemSize) {
                        NavigationBarItem(icon = {
                            Icon(
                                imageVector = items[i+itemSize].icon,
                                contentDescription = items[i+itemSize].title
                            )
                        },
                            label = { Text(text = items[i+itemSize].title) },
                            selected = navController.currentBackStackEntry?.destination?.navigatorName == items[i+itemSize].route,
                            onClick = {
                                println("Item Route : ${items[i+itemSize].route}")
                                navController.navigate(items[i+itemSize].route)
                            })
                    }
//                    IconButton(onClick = { /* Handle click */ }) {
//                        Icon(Icons.Default.Search, contentDescription = "Menu 3", tint = Color.White)
//                    }
//                    IconButton(onClick = { /* Handle click */ }) {
//                        Icon(Icons.Default.CalendarMonth, contentDescription = "Menu 4", tint = Color.White)
//                    }
                }
            }

        }


        Box(
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-28).dp)
                .border(8.dp, Color.Transparent, RoundedCornerShape(50))

        ) {
            FloatingActionButton(
                onClick = { /* Handle FAB click */ },
                contentColor = Color.White,
                shape = RoundedCornerShape(50),
//                elevation = FloatingActionButtonDefaults.elevation(26.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}
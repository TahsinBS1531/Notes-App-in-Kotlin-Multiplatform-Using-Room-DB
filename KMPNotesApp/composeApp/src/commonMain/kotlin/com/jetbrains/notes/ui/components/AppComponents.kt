package com.jetbrains.notes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jetbrains.notes.data.model.local.Note
import com.jetbrains.notes.ui.home.BottomNavItem
import com.jetbrains.notes.ui.home.HomeEvent
import com.jetbrains.notes.ui.home.HomeViewModel
import dev.icerock.moko.permissions.PermissionsController
import me.sample.library.resources.Res
import me.sample.library.resources.bulbimage
import me.sample.library.resources.mobilelogo
import me.sample.library.resources.website
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

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
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.primary
                    )
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
fun TaskCard(modifier: Modifier = Modifier, title: String, date: String, time: String) {
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
fun AppBottomBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
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
                val itemSize = items.size / 2
                Row {
                    for (i in 0..itemSize) {
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

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until itemSize) {
                        NavigationBarItem(icon = {
                            Icon(
                                imageVector = items[i + itemSize].icon,
                                contentDescription = items[i + itemSize].title
                            )
                        },
                            label = { Text(text = items[i + itemSize].title) },
                            selected = navController.currentBackStackEntry?.destination?.navigatorName == items[i + itemSize].route,
                            onClick = {
                                println("Item Route : ${items[i + itemSize].route}")
                                navController.navigate(items[i + itemSize].route)
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
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun AppNewMiniCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.mobilelogo),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(70.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.align(Alignment.BottomStart),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Mobile", style = MaterialTheme.typography.titleLarge)
                    Text(text = "6 Tasks", style = MaterialTheme.typography.bodyMedium)

                }
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight()
        ) {
            AppNewMiniCard2(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                title = "Wireframe",
                tasks = "10",
                painterResources = Res.drawable.bulbimage,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
            AppNewMiniCard2(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                title = "Website",
                tasks = "8",
                painterResources = Res.drawable.website,
                color = MaterialTheme.colorScheme.tertiaryContainer
            )

        }
    }
}

@Composable
fun AppNewMiniCard2(
    modifier: Modifier = Modifier, title: String, tasks: String,
    painterResources: DrawableResource,
    color: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {

            Image(
                painter = painterResource(painterResources),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp).weight(1f),
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1.5f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "$tasks Tasks",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun FormWithTabRow(
    modifier: Modifier = Modifier,
    titles: List<String>,
    tabContents: List<@Composable () -> Unit>,
    onEvent: (HomeEvent) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth().clip(RoundedCornerShape(6.dp)),
            divider = {},
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .padding(start = 12.dp, end = 12.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            for (i in titles.indices) {
                val isSelected = selectedTabIndex == i

                Tab(
                    selected = selectedTabIndex == i,
                    onClick = {
                        selectedTabIndex = i
                        println("Selected Tab: ${titles[i]}")
                        onEvent(HomeEvent.getLabelList(titles[i]))
                    },
                    selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
//                        .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
//                    unselectedContentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Text(
                        text = titles[i],
                        modifier = Modifier.padding(
                            top = 16.dp,
                            bottom = 16.dp,
                            start = 4.dp,
                            end = 4.dp
                        ),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize().padding(vertical = 16.dp)
//                .padding(16.dp)
        ) {
            tabContents[selectedTabIndex]()
        }
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun NoteListCard(note: Note, navController: NavController) {
    println("Note Labels : ${note.labels}")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp).clickable {
                navController.navigate("taskDetail/${note.id}")
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Chip(
                    onClick = {},
                    modifier = Modifier.align(Alignment.CenterStart),
                    colors = ChipDefaults.chipColors(
                        backgroundColor = when (note.priority) {
                            "Urgent" -> MaterialTheme.colorScheme.errorContainer
                            "Important" -> MaterialTheme.colorScheme.errorContainer
                            "Low" -> MaterialTheme.colorScheme.tertiaryContainer
                            "Delegated" -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.error
                        },
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(note.priority)
                }
                FlowRow(modifier = Modifier.align(Alignment.CenterEnd)) {
                    note.labels.forEach { label ->
                        Chip(onClick = {}) {
                            Text(label)
                        }
                    }
                }
            }
            Text(
                note.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(0.6f).weight(1f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Due : ${note.dateOfBirth}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(
                            Alignment.CenterStart
                        )
                    )
                    Text(
                        "${(note.progress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(
                            Alignment.CenterEnd
                        )
                    )

                }
                LinearProgressIndicator(
                    progress = { note.progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    trackColor = MaterialTheme.colorScheme.surfaceDim,
                )
            }
        }
    }
}
package com.jetbrains.notes.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.AssuredWorkload
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.jetbrains.notes.R
import com.jetbrains.notes.ui.home.showDatePicker
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType

@Composable
fun AppAddProject() {
    Column(modifier = Modifier.fillMaxSize()) {

    }
}

@Composable
fun AppMiniCard(modifier: Modifier = Modifier) {
    var selectedItem by remember {
        mutableStateOf(DropdownItem("Work", Icons.Default.Work))
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    val dropdownItems = listOf(
        DropdownItem("Work", Icons.Default.Work),
        DropdownItem("Office", Icons.Default.AssuredWorkload),
        DropdownItem("Personal", Icons.Default.GroupWork),
        DropdownItem("Daily", Icons.Default.WorkspacePremium)
    )

    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(selectedItem.icon, contentDescription = "Work")
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(text = "Task Group", style = MaterialTheme.typography.bodySmall)
                    Text(text = selectedItem.title, style = MaterialTheme.typography.titleMedium)
                }
            }

            IconButton(onClick = { expanded = !expanded }) {
                if (expanded) {
                    Icon(Icons.Default.ArrowCircleUp, contentDescription = null)
                } else {
                    Icon(Icons.Default.ArrowCircleDown, contentDescription = null)
                }
            }
            if (expanded) {
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    dropdownItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.title) },
                            onClick = {
                                selectedItem = item
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(item.icon, contentDescription = item.title)
                            }
                        )
                    }

                }
            }
        }


    }
}

data class DropdownItem(
    val title: String,
    val icon: ImageVector,
)

@Preview(showBackground = true, heightDp = 760, widthDp = 360)
@Composable
fun AppMiniCardPreview() {

    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AppMiniCard()

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTittleInputCard(label: String, modifier: Modifier = Modifier, height: Dp) {
    var text by remember { mutableStateOf("Grocery Shopping App") }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = label) },
                visualTransformation = VisualTransformation.None,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height),
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorIndicatorColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    errorLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    errorTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    errorPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 760, widthDp = 360)
@Composable
fun AppTittleInputCardPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AppTittleInputCard("Project Name", Modifier, 60.dp)
        }
    }
}

@Preview(showBackground = true, heightDp = 760, widthDp = 360)
@Composable
fun AppTittleInputCardPreview2() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AppTittleInputCard("Description", Modifier, 160.dp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppDatePickerCard(modifier: Modifier = Modifier, label: String) {
    var selectedItem by remember {
        mutableStateOf("01 May, 2022")
    }
    var expanded by remember {
        mutableStateOf(false)
    }


    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Work",
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(text = label, style = MaterialTheme.typography.bodySmall)
                    Text(text = selectedItem, style = MaterialTheme.typography.titleMedium)
                }
            }

            IconButton(onClick = { expanded = !expanded }) {
                if (expanded) {
                    Icon(
                        Icons.Default.ArrowCircleUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                } else {
                    Icon(
                        Icons.Default.ArrowCircleDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
            if (expanded) {
                showDatePicker(onDateSelected = {
                    selectedItem = it
                }, onDismiss = { expanded = false })
            }
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, heightDp = 760, widthDp = 360)
@Composable
fun AppDatePickerCardPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AppDatePickerCard(Modifier, "Start Date")
        }
    }
}

@Composable
fun AppUploadLogo() {
    val imageType = PickerType.Image
    val singleMode = PickerMode.Single

    val launcher = rememberFilePickerLauncher(
        type = imageType,
        mode = singleMode,
        title = "Pick a Logo",
        initialDirectory = "/custom/initial/path"
    ) { files ->

    }

    val saver = rememberFileSaverLauncher() { file ->
        file?.uri?.path?.let {
            println("Saved file: $it")
            // Handle the saved file
        }
    }
    saver.launch(
        baseName = "myTextFile",
        extension = "jpg",
        initialDirectory = "/custom/initial/path",
        bytes = ByteArray(0)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Upload Logo", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { launcher.launch() }) {
                Icon(
                    Icons.Default.Upload,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 760, widthDp = 360)
@Composable
fun AppUploadLogoPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AppUploadLogo()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddProjectScreen() {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.taskui),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppMiniCard()
            AppTittleInputCard("Project Name", Modifier, 60.dp)
            AppTittleInputCard("Description", Modifier, 160.dp)
            AppDatePickerCard(Modifier, "Start Date")
            AppDatePickerCard(Modifier, "End Date")
            AppUploadLogo()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, heightDp = 760, widthDp = 360)
@Composable
fun AddProjectScreenPreview() {
    AppTheme {
        AddProjectScreen()
    }
}

@Composable
fun AppNewMiniCard() {
    Row(
        modifier = Modifier
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Image(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.mobilelogo),
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
//                    .padding(16.dp)
                    .weight(1f),
                title = "Wireframe",
                tasks = "10",
                R.drawable.bulbimage
            )
            AppNewMiniCard2(
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(16.dp)
                    .weight(1f),
                title = "Website",
                tasks = "8",
                painterResources = R.drawable.website
            )

        }
    }

}

@Composable
fun AppNewMiniCard2(
    modifier: Modifier = Modifier, title: String, tasks: String,
    painterResources: Int
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Image(
                painter = painterResource(id = painterResources),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .weight(1f),
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


@Preview(showBackground = true)
@Composable
fun AppNewMiniCardPreview() {
    AppTheme {
        AppNewMiniCard()
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteListCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Chip(onClick = {}, modifier = Modifier.align(Alignment.CenterStart)) {
                    Text("Urgent")
                }
                Chip(onClick = {}, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text("Mobile")
                }
            }
            Text(
                "Make Changes to my Chat Project",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(0.6f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Due : 25 Oct",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(
                        Alignment.CenterStart
                    )
                )
                Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text("45%", style = MaterialTheme.typography.bodyMedium)
                    LinearProgressIndicator(
                        progress = { 0.45f },
                        modifier = Modifier.width(100.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        trackColor = MaterialTheme.colorScheme.surfaceDim,
                    )

                }
            }
        }
    }
}

@Preview
@Composable
fun NoteListCardPreview() {
    AppTheme {
        NoteListCard()
    }
}



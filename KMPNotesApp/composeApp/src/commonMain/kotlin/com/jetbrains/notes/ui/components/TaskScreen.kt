package com.jetbrains.notes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jetbrains.notes.ui.home.BottomNavItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskDetail(
    modifier: Modifier = Modifier,
    subtask: List<String>,
    task: String,
    progress: Float,
    taskType: String,
    dueDate: String,
    labels: List<String>,
    priority: String,
    description: String,
    onUpdateTaskStatus: (String) -> Unit,
    onUpdateSubtask: (List<String>) -> Unit,
    onUpdateProgression: (Float) -> Unit,
    onUpdateCompletedSubtasks: (List<String>) -> Unit,
    navController: NavController,
    completedSubTask: List<String>
) {
    val checkedStates = remember { mutableStateListOf(*Array(subtask.size) { false }) }

    fun updateProgression() {
        val completedCount = checkedStates.count { it }
        val newProgress = completedCount.toFloat() / subtask.size
        onUpdateProgression(newProgress)
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = task,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Chip(onClick = {}) {
                Text(text = "Medium")
            }
            Chip(onClick = {}) {
                Text(text = "Website")
            }
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Row {
                Text(text = "Due : $dueDate", modifier = Modifier.weight(1f))
                Text(text = "${(progress * 100).toInt()}%", modifier = Modifier)
            }
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                trackColor = MaterialTheme.colorScheme.surfaceDim,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )

        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Description", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = description,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Subtasks")
                if (completedSubTask.isNotEmpty()) {
                    completedSubTask.forEach {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceDim)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = true, onCheckedChange = {
                                }, enabled = false)
                                Text(
                                    text = it,
                                    modifier = Modifier.fillMaxWidth(0.8f),
                                    maxLines = 4,
                                    overflow = TextOverflow.Ellipsis,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            }
                        }
                    }
                }
                subtask.forEachIndexed { index, it ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = if (checkedStates[index]) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = checkedStates[index], onCheckedChange = {
                                checkedStates[index] = it
                                updateProgression()
                            }, enabled = taskType != "To Do")
                            Text(
                                it,
                                textDecoration = if (checkedStates[index]) TextDecoration.LineThrough else TextDecoration.None
                            )
                        }
                    }
                }
            }
        }
        val completedSubtasks = subtask.filterIndexed { index, _ -> checkedStates[index] }
        val updatedSubtasks = subtask.filterNot { it in completedSubtasks }

        if (taskType == "To Do") {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onUpdateTaskStatus("In Progress") }) {
                Text(text = "Start Task")
            }
        } else if (taskType == "In Progress" && updatedSubtasks.isNotEmpty()) {
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                onUpdateSubtask(updatedSubtasks)
                onUpdateCompletedSubtasks(completedSubtasks + completedSubTask)
                navController.navigate(BottomNavItem.Home.route)
            }) {
                Text(text = "Update Task")
            }
        } else if (taskType == "In Progress" && updatedSubtasks.isEmpty()) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onUpdateTaskStatus("Completed") }) {
                Text(text = "Complete Task")
                navController.navigate(BottomNavItem.Home.route)
            }
        }

    }
}
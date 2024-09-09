package com.jetbrains.notes.ui

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskDetail(modifier: Modifier = Modifier, subtask:List<String>,task:String, progress: Float,taskType:String) {
    val checkedStates = remember { mutableStateListOf(*Array(subtask.size) { false }) }

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
                Text(text = "Due : 25 Oct", modifier = Modifier.weight(1f))
                Text(text = "45%", modifier = Modifier)
            }
            LinearProgressIndicator(
                progress = { progress },
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
                    "Project Goal: To form and present a product strategy aimed at improving the company's competitiveness and increasing it's profits",
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
                subtask.forEachIndexed { index, it ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = if(checkedStates[index]) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = checkedStates[index], onCheckedChange = {
                                checkedStates[index] = it
                            }, enabled = taskType != "To Do")
                            Text(it)
                        }
                    }
                }
            }
        }

        if(taskType=="To Do"){
            Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                Text(text = "Start Task")
            }
        }else{
            Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                Text(text = "Complete Task")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailPreview() {
    AppTheme {
        TaskDetail(modifier = Modifier.padding(16.dp), task = "Prepare a product strategy presentation",
            subtask = listOf("Create a presentation", "Create a presentation"),
            progress = 0.45f, taskType = "In Progress"
        )
    }
}
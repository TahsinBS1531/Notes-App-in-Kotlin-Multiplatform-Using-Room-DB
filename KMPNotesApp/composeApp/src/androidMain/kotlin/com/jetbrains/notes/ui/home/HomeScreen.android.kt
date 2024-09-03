package com.jetbrains.notes.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.jetbrains.notes.ui.components.AppSearchBar
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun showDatePicker(
    onDateSelected: (String) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(onDismissRequest = { onDismiss() }, confirmButton = {
        Button(onClick = {
            onDateSelected(selectedDate)
            onDismiss()
        }

        ) {
            Text(text = "OK")
        }
    }, dismissButton = {
        Button(onClick = {
            onDismiss()
        }) {
            Text(text = "Cancel")
        }
    }) {
        DatePicker(
            state = datePickerState
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@RequiresApi(Build.VERSION_CODES.O)
actual fun formatTimestampToDateTime(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val formatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
actual fun taskDatePicker(
    onDateSelected: (String) -> Unit, onDismiss: () -> Unit, modifier: Modifier
) {
    val state = rememberDatePickerState()

    val selectedDate = state.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(modifier = modifier.fillMaxWidth()) {
        DatePicker(
            state = state,
            modifier = Modifier
                .shadow(elevation = 2.dp)
                .align(Alignment.Center),
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                headlineContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                yearContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                dayContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            headline = {
                Text(
                    text = "Add a Task",
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                )
            },
            title = {
                Text(
                    text = "Select a date to add a new task",
                    modifier = Modifier.padding(16.dp)
                )
            }
        )
    }

    if (selectedDate.isNotEmpty()) {
        onDateSelected(selectedDate)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun ShowTimePicker(
    onConfirm: (TimePickerState) -> Unit, onDismiss: () -> Unit
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        TimePicker(
            state = timePickerState,
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
            Button(onClick = {
                onConfirm(timePickerState)
                onDismiss()
            }) {
                Text("Confirm")
            }
        }

    }
}
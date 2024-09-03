package com.jetbrains.notes.ui.home

import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun showDatePicker(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}


private fun convertMillisToDate(millis: Long): String {
    // Convert milliseconds to seconds
    let seconds = millis / 1000
    let date = Date(timeIntervalSince1970: TimeInterval(seconds))

    // Create a DateFormatter to format the date
    let formatter = DateFormatter()
    formatter.dateFormat = "dd/MM/yyyy"

    // Return the formatted date string
    return formatter.string(from: date)
}

actual fun formatTimestampToDateTime(timestamp: Long): String {
    let seconds = timestamp / 1000
    let date = Date(timeIntervalSince1970: TimeInterval(seconds))

    // Create a DateFormatter to format the date
    let formatter = DateFormatter()
    formatter.dateFormat = "dd/MM/yyyy HH:mm:ss"

    // Return the formatted date string
    return formatter.string(from: date)
}

@Composable
actual fun taskDatePicker(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier
) {
}

@Composable
actual fun ShowTimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit
) {
}
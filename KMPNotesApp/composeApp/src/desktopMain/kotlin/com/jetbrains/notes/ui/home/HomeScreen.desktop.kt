package com.jetbrains.notes.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun showDatePicker(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {

//    val selectedYear = remember { mutableStateOf(LocalDate.now().year) }
////    val selectedMonth = remember { mutableStateOf(LocalDate.now().monthValue) }
////    val selectedDay = remember { mutableStateOf(LocalDate.now().dayOfMonth) }
////
////    Column(horizontalAlignment = Alignment.CenterHorizontally) {
////        Row(horizontalArrangement = Arrangement.SpaceBetween) {
////            NumberPicker(label = "Year", value = selectedYear)
////            NumberPicker(label = "Month", value = selectedMonth, range = 1..12)
////            NumberPicker(label = "Day", value = selectedDay, range = 1..31)
////        }
////
////        Button(onClick = {
////            val selectedDate = LocalDate.of(selectedYear.value, selectedMonth.value, selectedDay.value)
////            onDateSelected(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
////            onDismiss()
////        }) {
////            Text("OK")
////        }
////
////        Button(onClick = { onDismiss() }) {
////            Text("Cancel")
////        }
////    }

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

@Composable
fun NumberPicker(label: String, value: MutableState<Int>, range: IntRange = (1900..2100)) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$label: ")
        Button(onClick = { if (value.value > range.first) value.value-- }) {
            Text("-")
        }
        Text(text = value.value.toString())
        Button(onClick = { if (value.value < range.last) value.value++ }) {
            Text("+")
        }
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

actual fun formatTimestampToDateTime(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}
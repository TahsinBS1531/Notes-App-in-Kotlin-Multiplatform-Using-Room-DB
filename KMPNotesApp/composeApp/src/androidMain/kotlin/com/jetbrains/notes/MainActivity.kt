package com.jetbrains.notes

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.jetbrains.notes.data.core.NotificationScheduler
import com.jetbrains.notes.data.core.TaskReminderManager
import com.jetbrains.notes.ui.components.AppSearchBar
import com.jetbrains.notes.ui.components.TaskCard
import com.jetbrains.notes.ui.home.taskDatePicker

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val notificationScheduler = NotificationScheduler(applicationContext)
            val dao = getDatabaseBuilder(applicationContext).getDao()
            TaskReminderManager().scheduleTodayTasksNotifications(
                dao,notificationScheduler
            )
            AppTheme {
                App(dao,notificationScheduler)
            }

        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
//    App()
}

@Preview
@Composable
fun AppSearchBarPrev(){
    AppSearchBar(modifier = Modifier.fillMaxWidth(), onEvent = {})

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, heightDp = 600, widthDp = 350)
@Composable
fun datePreev(){
    taskDatePicker(onDateSelected = {}, onDismiss = {}, modifier = Modifier.fillMaxWidth())
}

@Preview(showBackground = true)
@Composable
fun prevTaskCard(){
    TaskCard(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp), title = "Task Title",
        date = "2023-09-12",
        time = "10:00 AM")
}
package com.jetbrains.notes.data.core
import com.jetbrains.notes.data.model.local.NotesDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

expect  class NotificationScheduler {
    fun scheduleNotification(time: Long, message: String)
}


expect class TaskReminderManager{

    fun scheduleTodayTasksNotifications(notesDao: NotesDao,notificationScheduler: NotificationScheduler)
}


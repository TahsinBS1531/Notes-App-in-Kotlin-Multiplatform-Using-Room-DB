package com.jetbrains.notes.data.core

actual abstract class NotificationScheduler {
    actual abstract fun scheduleNotification(time: Long, message: String)
}

actual class TaskReminderManager {
    actual fun scheduleTodayTasksNotifications() {
    }

}
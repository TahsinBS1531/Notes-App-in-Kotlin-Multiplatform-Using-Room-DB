package com.jetbrains.notes.data.core

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.jetbrains.notes.data.model.local.NotesDao
import com.jetbrains.notes.ui.home.convertMillisToDate
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Locale

actual class NotificationScheduler(private val context: Context) {
    @SuppressLint("ServiceCast", "ScheduleExactAlarm")
    actual fun scheduleNotification(time: Long, message: String) {
        createNotificationChannel()

        // Set up an AlarmManager to trigger the notification at the specified time
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("notification_message", message)
        }
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Task Reminder"
            val descriptionText = "Channel for Task Reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TASK_REMINDER_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("notification_message")

        val notificationBuilder = NotificationCompat.Builder(context, "TASK_REMINDER_CHANNEL")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Task Reminder")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }
}

actual class TaskReminderManager {
    @RequiresApi(Build.VERSION_CODES.O)
    actual fun scheduleTodayTasksNotifications(
        notesDao: NotesDao,
        notificationScheduler: NotificationScheduler
    ) = runBlocking {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val currentDate = currentDateTime.date.toString()
        val currentTime = currentDateTime.time.toString()

        val tasksForToday = notesDao.getTasksForToday(currentDate, currentTime).first()

        tasksForToday.forEach { note ->
            val notificationTime = convertTimeToMillis(note.dateOfBirth,note.selectedTime)
            notificationScheduler.scheduleNotification(notificationTime, "Reminder for task: ${note.title}")
        }

    }

    fun convertTimeToMillis(date: String, time: String): Long {
        val dateTimeString = "$date $time"
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val dateTime = format.parse(dateTimeString)
        return dateTime?.time ?: 0L
    }

}
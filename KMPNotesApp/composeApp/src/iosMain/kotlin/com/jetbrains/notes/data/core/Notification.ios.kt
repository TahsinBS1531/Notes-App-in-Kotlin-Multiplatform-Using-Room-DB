package com.jetbrains.notes.data.core

import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnit
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import platform.darwin.dispatch_get_main_queue
import kotlin.time.DurationUnit
import kotlin.time.toDuration

actual  class NotificationScheduler {
    actual fun scheduleNotification(time: Long, message: String){
        UNUserNotificationCenter.currentNotificationCenter().requestAuthorizationWithOptions(
            options = (1 shl 0) or (1 shl 2),
            completionHandler = { granted, error ->
                if (granted) {
                    // Schedule the notification
                    val content = UNMutableNotificationContent().apply {
                        body = message
                        sound = platform.UserNotifications.UNNotificationSound.defaultSound
                    }

                    // Calculate time difference
                    val timeInterval = (time - NSDate().timeIntervalSince1970).toDuration(DurationUnit.SECONDS)

                    val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
                        timeInterval = timeInterval.inWholeSeconds.toDouble(),
                        repeats = false
                    )

                    val request = UNNotificationRequest.requestWithIdentifier(
                        identifier = "task_reminder",
                        content = content,
                        trigger = trigger
                    )

                    UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request, null)
                }
            }
        )
    }
}

actual class TaskReminderManager {
    actual fun scheduleTodayTasksNotifications() {
    }

}
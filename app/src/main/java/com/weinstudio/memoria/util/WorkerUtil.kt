package com.weinstudio.memoria.util

import java.util.*

object WorkerUtil {

    private const val NOTIFY_HOUR = 10
    private const val NOTIFY_MINUTE = 0
    const val WORK_TAG = "daily_notifications"

    const val NOTIFICATION_ID = 10002
    const val CHANNEL_ID = "daily_notifications"
    const val PREFERENCES_KEY = "notify_upcoming"

    fun getWorkerInitialDelay(): Long {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, NOTIFY_HOUR)
        dueDate.set(Calendar.MINUTE, NOTIFY_MINUTE)

        dueDate.set(Calendar.SECOND, 0)
        dueDate.set(Calendar.MILLISECOND, 0)

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        return dueDate.timeInMillis - currentDate.timeInMillis
    }
}
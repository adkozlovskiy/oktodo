package com.weinstudio.memoria.util

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.weinstudio.memoria.service.NotificationWorker
import java.util.*
import java.util.concurrent.TimeUnit

object WorkerUtil {

    private const val NOTIFY_HOUR = 10
    private const val NOTIFY_MINUTE = 0
    private const val WORK_TAG = "daily_work"

    const val NOTIFICATION_ID = 10002
    const val CHANNEL_ID = "daily_notifications"
    const val PREFERENCES_KEY = "notify_upcoming"

    private fun getWorkerInitialDelay(): Long {
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

    fun enqueueNotificationWork(context: Context) {
        val timeDiff = getWorkerInitialDelay()

        val dailyWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(WORK_TAG, ExistingWorkPolicy.REPLACE, dailyWorkRequest)
    }
}
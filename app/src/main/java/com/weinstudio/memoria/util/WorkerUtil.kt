package com.weinstudio.memoria.util

import android.content.Context
import androidx.work.*
import com.weinstudio.memoria.service.NotificationWorker
import com.weinstudio.memoria.service.QueryWorker
import java.util.*
import java.util.concurrent.TimeUnit

object WorkerUtil {

    private fun getNotificationInitialDelay(): Long {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, NotificationWorker.NOTIFY_HOUR)
        dueDate.set(Calendar.MINUTE, NotificationWorker.NOTIFY_MINUTE)

        dueDate.set(Calendar.SECOND, 0)
        dueDate.set(Calendar.MILLISECOND, 0)

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        return dueDate.timeInMillis - currentDate.timeInMillis
    }

    fun enqueueNotificationWork(context: Context) {
        val timeDiff = getNotificationInitialDelay()

        val dailyWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag(NotificationWorker.WORK_TAG)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                NotificationWorker.WORK_TAG,
                ExistingWorkPolicy.REPLACE,
                dailyWorkRequest
            )
    }

    fun enqueueQueryWork(context: Context, type: String, body: String) {
        val inputData = Data.Builder()

        with(inputData) {
            putString(QueryWorker.QUERY_TYPE_EXTRA_TAG, type)
            putString(QueryWorker.QUERY_BODY_EXTRA_TAG, body)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val queryRequest = OneTimeWorkRequestBuilder<QueryWorker>()
            .addTag(QueryWorker.WORK_TAG)
            .setInputData(inputData.build())
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueue(queryRequest)
    }
}
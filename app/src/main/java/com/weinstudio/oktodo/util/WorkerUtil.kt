package com.weinstudio.oktodo.util

import android.content.Context
import androidx.work.*
import com.weinstudio.oktodo.service.QueryWorker
import com.weinstudio.oktodo.service.SyncWorker
import com.weinstudio.oktodo.service.UnfulfilledWorker
import java.util.*
import java.util.concurrent.TimeUnit

object WorkerUtil {

    private fun getNotificationInitialDelay(): Long {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, UnfulfilledWorker.NOTIFY_HOUR)
        dueDate.set(Calendar.MINUTE, UnfulfilledWorker.NOTIFY_MINUTE)

        dueDate.set(Calendar.SECOND, 0)
        dueDate.set(Calendar.MILLISECOND, 0)

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        return dueDate.timeInMillis - currentDate.timeInMillis
    }

    /**
     * @see UnfulfilledWorker
     */
    fun enqueueNotificationWork(context: Context) {
        val timeDiff = getNotificationInitialDelay()

        val dailyWorkRequest = OneTimeWorkRequestBuilder<UnfulfilledWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag(UnfulfilledWorker.WORK_TAG)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                UnfulfilledWorker.WORK_TAG,
                ExistingWorkPolicy.REPLACE,
                dailyWorkRequest
            )
    }

    /**
     * @see SyncWorker
     */
    fun enqueueSyncWork(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInitialDelay(SyncWorker.WORK_DELAY_HOURS, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(SyncWorker.WORK_TAG)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                SyncWorker.WORK_TAG,
                ExistingWorkPolicy.KEEP,
                syncWorkRequest
            )
    }

    /**
     * @see QueryWorker
     */
    fun enqueueQueryWork(context: Context, type: String, body: String?) {
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
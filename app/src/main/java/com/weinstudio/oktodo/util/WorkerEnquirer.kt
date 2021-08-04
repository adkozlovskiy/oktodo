package com.weinstudio.oktodo.util

import android.app.Application
import androidx.work.*
import com.google.gson.Gson
import com.weinstudio.oktodo.data.model.Problem
import com.weinstudio.oktodo.worker.DailyNotificationsWorker
import com.weinstudio.oktodo.worker.PeriodicallySyncWorker
import com.weinstudio.oktodo.worker.QueryWorker
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class WorkerEnquirer @Inject constructor(
    private val appContext: Application,
    private val gson: Provider<Gson>
) {

    fun enqueueDailyNotifications() {
        DailyNotificationsWorker.enqueueNotificationWork(appContext)
    }

    fun enqueuePeriodicallySync() {
        PeriodicallySyncWorker.enqueuePeriodicallySync(appContext)
    }

    fun enqueueInsert(entryProblem: Problem) {
        enqueueQuery(QueryWorker.QUERY_TYPE_INSERT, entryProblem)
    }

    fun enqueueUpdate(entryProblem: Problem) {
        enqueueQuery(QueryWorker.QUERY_TYPE_UPDATE, entryProblem)
    }

    fun enqueueDelete(entryProblem: Problem) {
        enqueueQuery(QueryWorker.QUERY_TYPE_DELETE, entryProblem)
    }

    private fun enqueueQuery(type: String, entryProblem: Problem?) {
        val body = gson.get().toJson(entryProblem)
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

        WorkManager.getInstance(appContext).enqueue(queryRequest)
    }
}
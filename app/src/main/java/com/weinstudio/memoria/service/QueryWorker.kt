package com.weinstudio.memoria.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.weinstudio.memoria.data.api.RetrofitClient
import com.weinstudio.memoria.data.entity.Problem

class QueryWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    companion object {

        const val QUERY_TYPE_EXTRA_TAG = "query_type"
        const val QUERY_BODY_EXTRA_TAG = "query_body"

        const val QUERY_TYPE_INSERT = 0
        const val QUERY_TYPE_UPDATE = 1
        const val QUERY_TYPE_DELETE = 2

    }

    private val remoteSource = RetrofitClient.retrofitServices

    override suspend fun doWork(): Result {

        // Getting query type nad body (serialized problem)
        val queryType = inputData.getInt(QUERY_TYPE_EXTRA_TAG, QUERY_TYPE_INSERT)
        val bodyString = inputData.getString(QUERY_BODY_EXTRA_TAG)

        if (bodyString.isNullOrEmpty()) {
            return Result.failure()
        }

        // Deserialize problem
        val problem = Gson().fromJson(bodyString, Problem::class.java)

        return when (queryType) {
            QUERY_TYPE_INSERT -> insertProblem(problem)

            QUERY_TYPE_UPDATE -> updateProblem(problem)

            QUERY_TYPE_DELETE -> deleteProblem(problem)

            else -> Result.failure()
        }
    }

    private suspend fun insertProblem(problem: Problem): Result {
        remoteSource.insert(problem)
        return Result.success()
    }

    private suspend fun updateProblem(problem: Problem): Result {
        remoteSource.update(problem.id, problem)
        return Result.success()
    }

    private suspend fun deleteProblem(problem: Problem): Result {
        remoteSource.delete(problem.id)
        return Result.success()
    }
}
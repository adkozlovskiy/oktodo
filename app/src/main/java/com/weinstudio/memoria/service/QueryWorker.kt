package com.weinstudio.memoria.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.weinstudio.memoria.data.api.RetrofitClient
import com.weinstudio.memoria.data.entity.Problem
import retrofit2.Response

class QueryWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {

        const val WORK_TAG = "query_work"

        const val QUERY_TYPE_EXTRA_TAG = "query_type"
        const val QUERY_BODY_EXTRA_TAG = "query_body"

        const val QUERY_TYPE_INSERT = "0"
        const val QUERY_TYPE_UPDATE = "1"
        const val QUERY_TYPE_DELETE = "2"

    }

    private val remoteSource = RetrofitClient.retrofitServices

    override suspend fun doWork(): Result {

        // Getting query type nad body (serialized problem)
        val queryType = inputData.getString(QUERY_TYPE_EXTRA_TAG)
        val bodyString = inputData.getString(QUERY_BODY_EXTRA_TAG)

        if (bodyString.isNullOrEmpty()) {
            return Result.failure()
        }

        // Deserialize problem
        val problem = Gson().fromJson(bodyString, Problem::class.java)

        Log.d("TAG", "doWork: $queryType - $problem")

        return when (queryType) {
            QUERY_TYPE_INSERT -> insertProblem(problem)

            QUERY_TYPE_UPDATE -> updateProblem(problem)

            QUERY_TYPE_DELETE -> deleteProblem(problem)

            else -> Result.failure()
        }
    }

    private suspend fun insertProblem(problem: Problem): Result {
        return handleResponse(remoteSource.insert(problem))
    }

    private suspend fun updateProblem(problem: Problem): Result {
        return handleResponse(remoteSource.update(problem.id, problem))
    }

    private suspend fun deleteProblem(problem: Problem): Result {
        return handleResponse(remoteSource.delete(problem.id))
    }

    private fun <T> handleResponse(response: Response<T>): Result {
        return if (response.isSuccessful) {
            Result.success()

        } else {
            when (response.code()) {
                404 -> Result.failure()
                else -> Result.retry()
            }
        }
    }
}
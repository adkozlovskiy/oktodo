package com.weinstudio.oktodo.service

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.weinstudio.oktodo.data.api.ProblemsService
import com.weinstudio.oktodo.data.db.dao.ProblemsDao
import com.weinstudio.oktodo.data.model.Problem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.Response

@HiltWorker
class QueryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val remoteSource: ProblemsService,
    private val localSource: ProblemsDao

) : CoroutineWorker(context, params) {

    companion object {

        const val WORK_TAG = "query_work"

        const val QUERY_TYPE_EXTRA_TAG = "query_type"
        const val QUERY_BODY_EXTRA_TAG = "query_body"

        const val QUERY_TYPE_REFRESH = "refresh"
        const val QUERY_TYPE_INSERT = "insert"
        const val QUERY_TYPE_UPDATE = "update"
        const val QUERY_TYPE_DELETE = "delete"

    }

    override suspend fun doWork(): Result {

        // Getting query type nad body (serialized problem)
        val queryType = inputData.getString(QUERY_TYPE_EXTRA_TAG)
        val bodyString = inputData.getString(QUERY_BODY_EXTRA_TAG)

        if (queryType != QUERY_TYPE_REFRESH && bodyString.isNullOrEmpty()) {
            return Result.failure()
        }

        // Deserialize problem
        val problem = Gson().fromJson(bodyString, Problem::class.java)

        Log.d(WORK_TAG, "$queryType - $problem")

        return when (queryType) {
            QUERY_TYPE_REFRESH -> refreshProblems()

            QUERY_TYPE_INSERT -> insertProblem(problem)

            QUERY_TYPE_UPDATE -> updateProblem(problem)

            QUERY_TYPE_DELETE -> deleteProblem(problem)

            else -> Result.failure()
        }
    }

    private suspend fun refreshProblems(): Result {
        val response = remoteSource.getAll()
        val remoteProblems = response.body()

        if (response.isSuccessful && remoteProblems != null) {
            val localProblems = localSource.getAll()

            for (remoteProblem in remoteProblems) {
                val localProblem = localProblems.find { it.id == remoteProblem.id }

                // If the server has added
                if (localProblem == null) {
                    localSource.insert(remoteProblem)

                    // If the server has updated
                } else if (remoteProblem.updated >= localProblem.updated) {
                    localSource.update(remoteProblem)

                }
            }

            for (localProblem in localProblems) {
                val remoteProblem = remoteProblems.find { it.id == localProblem.id }

                // If the server has deleted
                if (remoteProblem == null) {
                    localSource.delete(localProblem)
                }
            }

            return Result.success()

        }

        return Result.failure()
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
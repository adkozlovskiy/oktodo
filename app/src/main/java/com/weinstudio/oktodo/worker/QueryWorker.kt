package com.weinstudio.oktodo.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.weinstudio.oktodo.data.ProblemsRepository
import com.weinstudio.oktodo.data.model.Problem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.Response

@HiltWorker
class QueryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ProblemsRepository

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
        return repository.refreshProblems()
    }

    private suspend fun insertProblem(entryProblem: Problem): Result {
        val response = repository.insertProblemRemote(entryProblem)
        return handleResponse(response)
    }

    private suspend fun updateProblem(entryProblem: Problem): Result {
        val response = repository.updateProblemRemote(entryProblem)
        return handleResponse(response)
    }

    private suspend fun deleteProblem(entryProblem: Problem): Result {
        val response = repository.deleteProblemRemote(entryProblem)
        return handleResponse(response)
    }

    private fun handleResponse(response: Response<Problem>): Result {
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
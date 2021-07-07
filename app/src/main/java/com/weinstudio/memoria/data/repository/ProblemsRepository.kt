package com.weinstudio.memoria.data.repository

import android.content.Context
import com.google.gson.Gson
import com.weinstudio.memoria.data.api.RetrofitServices
import com.weinstudio.memoria.data.db.dao.ProblemsDao
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.service.QueryWorker
import com.weinstudio.memoria.util.WorkerUtil
import kotlinx.coroutines.flow.Flow

class ProblemsRepository(
    private val remoteSource: RetrofitServices,
    private val localSource: ProblemsDao,
    private val context: Context

) {

    fun getProblems(needFilter: Boolean): Flow<List<Problem>> {
        return localSource.getAllFlow(needFilter)
    }

    fun getCountFlow(done: Boolean): Flow<Int> {
        return localSource.getCountFlow(done)
    }

    fun getCount(done: Boolean): Int {
        return localSource.getCount(done)
    }

    suspend fun insertProblem(problem: Problem) {
        localSource.insert(problem)

        val body = Gson().toJson(problem)
        WorkerUtil.enqueueQueryWork(context, QueryWorker.QUERY_TYPE_INSERT, body)
    }

    suspend fun deleteProblem(problem: Problem) {
        localSource.delete(problem)

        val body = Gson().toJson(problem)
        WorkerUtil.enqueueQueryWork(context, QueryWorker.QUERY_TYPE_DELETE, body)
    }

    suspend fun updateProblem(problem: Problem) {
        localSource.update(problem)

        val body = Gson().toJson(problem)
        WorkerUtil.enqueueQueryWork(context, QueryWorker.QUERY_TYPE_UPDATE, body)
    }

    suspend fun refreshProblems() {

        val remoteProblems = remoteSource.getAll()
        val localProblems = localSource.getAll()

        for (remoteProblem in remoteProblems) {
            val localProblem = localProblems.find { it.id == remoteProblem.id }

            // If the server has added
            if (localProblem == null) {
                localSource.insert(remoteProblem)

                // If the server has updated
            } else if (remoteProblem.updated > localProblem.updated) {
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
    }
}
package com.weinstudio.memoria.data.repository

import android.content.Context
import com.google.gson.Gson
import com.weinstudio.memoria.data.api.RetrofitServices
import com.weinstudio.memoria.data.db.dao.ProblemsDao
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.service.QueryWorker
import com.weinstudio.memoria.util.WorkerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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

    fun insertProblem(problem: Problem) = CoroutineScope(Dispatchers.IO)
        .launch {
            localSource.insert(problem)

            val problemSerialized = Gson().toJson(problem)
            WorkerUtil.enqueueQueryWork(
                context = context,
                type = QueryWorker.QUERY_TYPE_INSERT,
                body = problemSerialized
            )
        }

    fun deleteProblem(problem: Problem) = CoroutineScope(Dispatchers.IO)
        .launch {
            localSource.delete(problem)

            val problemSerialized = Gson().toJson(problem)
            WorkerUtil.enqueueQueryWork(
                context = context,
                type = QueryWorker.QUERY_TYPE_DELETE,
                body = problemSerialized
            )
        }

    fun updateProblem(problem: Problem) = CoroutineScope(Dispatchers.IO)
        .launch {
            localSource.update(problem)

            val problemSerialized = Gson().toJson(problem)
            WorkerUtil.enqueueQueryWork(
                context = context,
                type = QueryWorker.QUERY_TYPE_UPDATE,
                body = problemSerialized
            )
        }

    fun refreshProblems() = CoroutineScope(Dispatchers.IO)
        .launch {
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
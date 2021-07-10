package com.weinstudio.memoria.data.repository

import android.content.Context
import com.google.gson.Gson
import com.weinstudio.memoria.data.db.dao.ProblemsDao
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.service.QueryWorker
import com.weinstudio.memoria.util.WorkerUtil
import kotlinx.coroutines.flow.Flow
import java.util.*

class ProblemsRepository(
    private val localSource: ProblemsDao,
    private val context: Context

) {

    private val gson by lazy { Gson() }

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
        with(problem) {
            id = "${Calendar.getInstance().timeInMillis}"
            created = Calendar.getInstance().timeInMillis / 1000
            updated = Calendar.getInstance().timeInMillis / 1000
        }

        localSource.insert(problem)

        val body = gson.toJson(problem)
        WorkerUtil.enqueueQueryWork(context, QueryWorker.QUERY_TYPE_INSERT, body)
    }

    suspend fun deleteProblem(problem: Problem) {
        localSource.delete(problem)

        val body = gson.toJson(problem)
        WorkerUtil.enqueueQueryWork(context, QueryWorker.QUERY_TYPE_DELETE, body)
    }

    suspend fun updateProblem(problem: Problem) {
        with(problem) {
            updated = Calendar.getInstance().timeInMillis / 1000
        }

        localSource.update(problem)

        val body = gson.toJson(problem)
        WorkerUtil.enqueueQueryWork(context, QueryWorker.QUERY_TYPE_UPDATE, body)
    }
}
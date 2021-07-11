package com.weinstudio.oktodo.data.repository

import android.content.Context
import com.google.gson.Gson
import com.weinstudio.oktodo.data.db.dao.ProblemsDao
import com.weinstudio.oktodo.data.entity.Problem
import com.weinstudio.oktodo.service.QueryWorker
import com.weinstudio.oktodo.util.WorkerUtil
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
        val millis = Calendar.getInstance().timeInMillis

        val entry = problem.copy(
            id = "$millis",
            created = millis / 1000,
            updated = millis / 1000
        )

        localSource.insert(entry)

        WorkerUtil.enqueueQueryWork(
            context, QueryWorker.QUERY_TYPE_INSERT, gson.toJson(entry)
        )
    }

    suspend fun deleteProblem(problem: Problem) {
        localSource.delete(problem)

        WorkerUtil.enqueueQueryWork(
            context, QueryWorker.QUERY_TYPE_DELETE, gson.toJson(problem)
        )
    }

    suspend fun updateProblem(problem: Problem) {
        val entry = problem.copy(
            updated = Calendar.getInstance().timeInMillis / 1000
        )

        localSource.update(entry)

        WorkerUtil.enqueueQueryWork(
            context, QueryWorker.QUERY_TYPE_UPDATE, gson.toJson(entry)
        )
    }

    fun enqueueRefreshProblems() {
        WorkerUtil.enqueueQueryWork(
            context, QueryWorker.QUERY_TYPE_REFRESH, null
        )
    }
}
package com.weinstudio.memoria.data.repository

import com.weinstudio.memoria.data.api.RetrofitServices
import com.weinstudio.memoria.data.db.dao.ProblemsDao
import com.weinstudio.memoria.data.entity.Problem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProblemsRepository(
    private val remoteSource: RetrofitServices,
    private val localSource: ProblemsDao

) {

    fun getProblems(needFilter: Boolean): Flow<List<Problem>> {
        // Getting fresh data from remote
        refreshProblems()

        // Return saved data
        return localSource.getAll(needFilter)
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
            remoteSource.insert(problem)
        }

    fun deleteProblem(problem: Problem) = CoroutineScope(Dispatchers.IO)
        .launch {
            localSource.delete(problem)
            remoteSource.delete(problem.id)
        }

    fun updateProblem(problem: Problem) = CoroutineScope(Dispatchers.IO)
        .launch {
            localSource.update(problem)
            remoteSource.update(problem.id, problem)
        }

    private fun refreshProblems() = CoroutineScope(Dispatchers.IO)
        .launch {
            val response = remoteSource.getAll()
            localSource.insertAll(response)
        }
}
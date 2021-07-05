package com.weinstudio.memoria.data.repository

import androidx.annotation.WorkerThread
import com.weinstudio.memoria.data.api.RetrofitServices
import com.weinstudio.memoria.data.db.dao.ProblemDao
import com.weinstudio.memoria.data.entity.Problem
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProblemRepository(
    private val remoteSource: RetrofitServices,
    private val localSource: ProblemDao

) {

    fun getProblems(filter: Boolean): Flow<List<Problem>> {
        refreshProblems()

        return if (filter) localSource.getUnfulfilled()
        else localSource.getAll()
    }

    fun getCountFlow(done: Boolean): Flow<Int> {
        return localSource.getCountFlow(done)
    }

    fun getCount(done: Boolean): Int {
        return localSource.getCount(done)
    }

    @WorkerThread
    suspend fun insertProblem(problem: Problem) {
        localSource.insert(problem)
        remoteSource.insert(problem)
    }

    @WorkerThread
    suspend fun deleteProblem(problem: Problem) {
        localSource.delete(problem)
        remoteSource.delete(problem.id)
    }

    @WorkerThread
    suspend fun updateProblem(problem: Problem) {
        localSource.update(problem)
    }

    @WorkerThread
    suspend fun changeStatus(problem: Problem, done: Boolean) {
        val newProblem = problem.copy(done = done)
        localSource.update(newProblem)
        remoteSource.update(problem.id, newProblem)
    }

    private fun refreshProblems() {
        remoteSource.getAll().enqueue(object : Callback<List<Problem>> {
            override fun onResponse(call: Call<List<Problem>>, response: Response<List<Problem>>) {
                if (!response.body().isNullOrEmpty()) {
                    localSource.insertAll(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Problem>>, t: Throwable) {
                // TODO: 05.07.2021
            }
        })
    }
}
package com.weinstudio.memoria.data.repository

import androidx.annotation.WorkerThread
import com.weinstudio.memoria.data.api.RetrofitServices
import com.weinstudio.memoria.data.db.dao.ProblemDao
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.entity.enums.Priority
import kotlinx.coroutines.flow.Flow
import java.util.*

class ProblemRepository(
    private val remoteSource: RetrofitServices,
    private val localSource: ProblemDao

) {

    // Local source.
    fun getProblems(filter: Boolean): Flow<List<Problem>> {
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
    }

    @WorkerThread
    suspend fun deleteProblem(problem: Problem) {
        localSource.delete(problem)
    }

    @WorkerThread
    suspend fun updateProblem(problem: Problem) {
        localSource.update(problem)
    }

    fun changeStatus(id: Int, status: Boolean) {
        localSource.changeStatus(id, status)
    }

//    // Remote source.
//    suspend fun loadProblems(): List<Problem> {
//
//    }

    private fun loadData(): List<Problem> {
        return listOf(
            Problem(
                1,
                "Покормить кота",
                1624552533145,
                Priority.HIGH, false, Date().time, Date().time
            ),
            Problem(
                2,
                "Помыть посуду",
                1624552533146,
                Priority.HIGH, false, Date().time, Date().time
            ),
            Problem(
                3,
                "Подготовиться к экзамену по английскому",
                null,
                Priority.HIGH, false, Date().time, Date().time
            ),
            Problem(
                4,
                "Купить подарок котику",
                null,
                Priority.HIGH, false, Date().time, Date().time
            )
        )
    }
}
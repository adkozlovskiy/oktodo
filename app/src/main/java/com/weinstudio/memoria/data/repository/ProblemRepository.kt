package com.weinstudio.memoria.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.asLiveData
import com.weinstudio.memoria.data.db.dao.ProblemDao
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.entity.enums.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class ProblemRepository(
//    private val remoteSource: ProblemServices,
    private val localSource: ProblemDao

) {

    init {
        CoroutineScope(Dispatchers.Main).launch { localSource.insertAll(loadData()) }
    }

    fun getProblems(filter: Boolean): Flow<List<Problem>> {
        return if (filter) localSource.getUnfulfilled()
        else localSource.getAll()
    }

    fun getCountFlowWithStatus(status: Boolean): Flow<Int> {
        return localSource.getCountWithStatus(status)
    }

    fun getCountWithStatus(status: Boolean): Int {
        val count = localSource.getCountWithStatus(status).asLiveData().value
        return count ?: 0
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
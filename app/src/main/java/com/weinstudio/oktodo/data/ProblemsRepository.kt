package com.weinstudio.oktodo.data

import androidx.work.ListenableWorker
import com.weinstudio.oktodo.data.api.ProblemsService
import com.weinstudio.oktodo.data.db.ProblemsDao
import com.weinstudio.oktodo.data.model.Problem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProblemsRepository @Inject constructor(
    private val localSource: ProblemsDao,
    private val remoteSource: ProblemsService,
) {

    fun getProblemsFlow(filtered: Boolean): Flow<List<Problem>> {
        return if (filtered) {
            localSource.getAllFlowFiltered()

        } else localSource.getAllFlow()
    }

    fun getCountFlow(done: Boolean): Flow<Int> {
        return localSource.getCountFlow(done)
    }

    suspend fun getCount(done: Boolean): Int {
        return localSource.getCount(done)
    }

    suspend fun insertProblem(entryProblem: Problem) {
        localSource.insert(entryProblem)
    }

    suspend fun updateProblem(entryProblem: Problem) {
        localSource.update(entryProblem)
    }

    suspend fun deleteProblem(entryProblem: Problem) {
        localSource.delete(entryProblem)
    }

    suspend fun insertProblemRemote(entryProblem: Problem): Response<Problem> {
        return remoteSource.insert(entryProblem)
    }

    suspend fun deleteProblemRemote(entryProblem: Problem): Response<Problem> {
        return remoteSource.delete(entryProblem.id)
    }

    suspend fun updateProblemRemote(entryProblem: Problem): Response<Problem> {
        return remoteSource.update(entryProblem.id, entryProblem)
    }

    suspend fun refreshProblems(): ListenableWorker.Result {
        val remoteResponse = remoteSource.getAll()

        if (remoteResponse.isSuccessful && remoteResponse.body() != null) {
            val remoteProblems = remoteResponse.body()!!
            val localProblems = localSource.getAll()

            for (remoteProblem in remoteProblems) {
                val localProblem = localProblems.find { localProblem ->
                    localProblem.id == remoteProblem.id
                }

                if (localProblem == null) {
                    localSource.insert(remoteProblem)

                } else if (remoteProblem.updated >= localProblem.updated) {
                    localSource.update(remoteProblem)
                }
            }

            for (localProblem in localProblems) {
                val remoteProblem = remoteProblems.find { remoteProblem ->
                    remoteProblem.id == localProblem.id
                }

                if (remoteProblem == null) {
                    localSource.delete(localProblem)
                }
            }

            return ListenableWorker.Result.success()
        }

        return ListenableWorker.Result.failure()
    }
}
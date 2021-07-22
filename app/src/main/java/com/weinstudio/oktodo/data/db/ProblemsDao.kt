package com.weinstudio.oktodo.data.db

import androidx.annotation.WorkerThread
import androidx.room.*
import com.weinstudio.oktodo.data.model.Problem
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemsDao {

    @Query(
        "SELECT * FROM problems " +
                "ORDER BY done ASC, deadline IS NULL, deadline ASC, importance DESC"
    )
    fun getAllFlow(): Flow<List<Problem>>

    @Query(
        "SELECT * FROM problems " +
                "WHERE done = 0 " +
                "ORDER BY done ASC, deadline IS NULL, deadline ASC, importance DESC"
    )
    fun getAllFlowFiltered(): Flow<List<Problem>>

    @Query("SELECT * FROM problems ")
    @WorkerThread
    suspend fun getAll(): List<Problem>

    @Query(
        "SELECT COUNT(*) " +
                "FROM problems " +
                "WHERE done = :done"
    )
    fun getCountFlow(done: Boolean): Flow<Int>

    @Query(
        "SELECT COUNT(*) " +
                "FROM problems " +
                "WHERE done = :done"
    )
    suspend fun getCount(done: Boolean): Int

    @Insert
    @WorkerThread
    suspend fun insert(problem: Problem)

    @Update
    @WorkerThread
    suspend fun update(problem: Problem)

    @Delete
    @WorkerThread
    suspend fun delete(problem: Problem)

}
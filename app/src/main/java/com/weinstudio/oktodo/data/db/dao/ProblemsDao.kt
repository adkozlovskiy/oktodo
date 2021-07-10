package com.weinstudio.oktodo.data.db.dao

import androidx.room.*
import com.weinstudio.oktodo.data.entity.Problem
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemsDao {

    /** Function of getting the flow of all problems
     * @param needFilter if true, then return only outstanding tasks,
     * otherwise - all
     */
    @Query(
        "SELECT * FROM problems " +
                "WHERE (:needFilter = 1 AND done = 0) OR :needFilter = 0 " +
                "ORDER BY done ASC, deadline IS NULL, deadline ASC, importance DESC"
    )
    fun getAllFlow(needFilter: Boolean): Flow<List<Problem>>

    @Query("SELECT * FROM problems ")
    fun getAll(): List<Problem>

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
    fun getCount(done: Boolean): Int

    @Insert
    suspend fun insert(problem: Problem)

    @Update
    suspend fun update(problem: Problem)

    @Delete
    suspend fun delete(problem: Problem)

}
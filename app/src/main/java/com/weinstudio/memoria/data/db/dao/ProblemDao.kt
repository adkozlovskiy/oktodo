package com.weinstudio.memoria.data.db.dao

import androidx.room.*
import com.weinstudio.memoria.data.entity.Problem
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemDao {

    @Query("SELECT * FROM problems ORDER BY done ASC, deadline IS NULL, deadline ASC, priority DESC")
    fun getAll(): Flow<List<Problem>>

    @Query("SELECT * FROM problems WHERE done = 0 ORDER BY done ASC, deadline IS NULL, deadline ASC, priority DESC")
    fun getUnfulfilled(): Flow<List<Problem>>

    @Query("UPDATE problems SET done = :status WHERE id = :id")
    fun changeStatus(id: Int, status: Boolean)

    @Query("SELECT COUNT(*) FROM problems WHERE done = :done")
    fun getCountWithStatus(done: Boolean): Flow<Int>

    @Insert
    suspend fun insert(problem: Problem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(problems: List<Problem>)

    @Update
    suspend fun update(problem: Problem)

    @Delete
    suspend fun delete(problem: Problem)

}
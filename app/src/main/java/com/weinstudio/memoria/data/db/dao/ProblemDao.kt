package com.weinstudio.memoria.data.db.dao

import androidx.room.*
import com.weinstudio.memoria.data.entity.Problem
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemDao {

    @Query("SELECT * FROM problems ORDER BY done ASC, deadline IS NULL, deadline ASC, importance DESC")
    fun getAll(): Flow<List<Problem>>

    @Query("SELECT * FROM problems WHERE done = 0 ORDER BY done ASC, deadline IS NULL, deadline ASC, importance DESC")
    fun getUnfulfilled(): Flow<List<Problem>>

    @Query("SELECT COUNT(*) FROM problems WHERE done = :done")
    fun getCountFlow(done: Boolean): Flow<Int>

    @Query("SELECT COUNT(*) FROM problems WHERE done = :done")
    fun getCount(done: Boolean): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(problems: List<Problem>)

    @Insert
    suspend fun insert(problem: Problem)

    @Update
    suspend fun update(problem: Problem)

    @Delete
    suspend fun delete(problem: Problem)

}
package com.weinstudio.memoria.data.db.dao

import androidx.room.*
import com.weinstudio.memoria.data.entity.Problem

@Dao
interface ProblemsDao {

    @Query("SELECT * FROM problems")
    fun getAll(): List<Problem>

    @Query("UPDATE problems SET done = :status WHERE id = :id")
    fun changeStatus(id: Int, status: Boolean)

    @Insert
    suspend fun insert(p: Problem)

    @Update
    suspend fun update(p: Problem)

    @Delete
    suspend fun delete(p: Problem)

}
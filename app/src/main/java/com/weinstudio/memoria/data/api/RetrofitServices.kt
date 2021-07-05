package com.weinstudio.memoria.data.api

import androidx.annotation.WorkerThread
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.entity.SyncRequest
import retrofit2.http.*

interface RetrofitServices {

    @GET("tasks")
    @WorkerThread
    suspend fun getAll(): List<Problem>

    @POST("tasks")
    @WorkerThread
    suspend fun insert(@Body p: Problem): Problem

    @DELETE("tasks/{id}")
    @WorkerThread
    suspend fun delete(@Path("id") id: String): Problem

    @PUT("tasks/{id}")
    @WorkerThread
    suspend fun update(@Path("id") id: String, @Body p: Problem): Problem

    @PUT("tasks")
    @WorkerThread
    suspend fun sync(@Body syncRequest: SyncRequest): List<Problem>

}
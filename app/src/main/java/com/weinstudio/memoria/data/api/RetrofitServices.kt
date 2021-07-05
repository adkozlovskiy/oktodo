package com.weinstudio.memoria.data.api

import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.entity.SyncRequest
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {

    @GET("tasks")
    fun getAll(): Call<List<Problem>>

    @POST("tasks")
    suspend fun insert(@Body p: Problem): Problem

    @DELETE("tasks/{id}")
    suspend fun delete(@Path("id") id: String): Problem

    @PUT("tasks/{id}")
    suspend fun update(@Path("id") id: String, @Body p: Problem): Problem

    @PUT("tasks")
    suspend fun sync(@Body syncRequest: SyncRequest): Call<Problem>

}
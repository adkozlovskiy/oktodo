package com.weinstudio.memoria.data.api

import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.entity.SyncRequest
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {

    @GET("tasks")
    suspend fun getAllProblems(): Flow<List<Problem>>

    @POST("tasks")
    suspend fun postProblem(@Body p: RequestBody): Call<Problem>

    @DELETE("tasks/{id}")
    suspend fun deleteProblem(@Path("id") id: Int): Call<Problem>

    @PUT("tasks/{id}")
    suspend fun putProblem(@Path("id") id: Int, @Body p: Problem): Call<Problem>

    @PUT("tasks")
    suspend fun syncProblems(@Body syncRequest: SyncRequest): Call<Problem>

}
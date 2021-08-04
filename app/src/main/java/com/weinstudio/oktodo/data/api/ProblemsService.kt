package com.weinstudio.oktodo.data.api

import com.weinstudio.oktodo.data.model.Problem
import retrofit2.Response
import retrofit2.http.*

interface ProblemsService {

    @GET("tasks")
    suspend fun getAll(): Response<List<Problem>>

    @POST("tasks")
    suspend fun insert(@Body p: Problem): Response<Problem>

    @DELETE("tasks/{id}")
    suspend fun delete(@Path("id") id: String): Response<Problem>

    @PUT("tasks/{id}")
    suspend fun update(@Path("id") id: String, @Body p: Problem): Response<Problem>

}
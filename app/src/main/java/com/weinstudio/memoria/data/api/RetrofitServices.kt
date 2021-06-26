package com.weinstudio.memoria.data.api

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitServices {

    @GET("/")
    fun getSomeData(): Call<MutableList<Any>>
}
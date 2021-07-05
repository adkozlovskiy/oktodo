package com.weinstudio.memoria.data.api

import com.weinstudio.memoria.data.api.interceptor.HeaderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://d5dps3h13rv6902lp5c8.apigw.yandexcloud.net/"

    private var retrofitInstance: Retrofit? = null

    private fun getClient(): Retrofit {
        val clientBuilder = OkHttpClient().newBuilder()

        // Interceptors
        clientBuilder.networkInterceptors().add(HeaderInterceptor())

        // Timeouts
        clientBuilder.readTimeout(1, TimeUnit.MINUTES)
        clientBuilder.writeTimeout(1, TimeUnit.MINUTES)
        clientBuilder.connectTimeout(1, TimeUnit.MINUTES)

        if (retrofitInstance == null) {
            retrofitInstance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build()
        }

        return retrofitInstance ?: throw IllegalStateException()
    }

    val retrofitServices: RetrofitServices
        get() = getClient().create(RetrofitServices::class.java)
}
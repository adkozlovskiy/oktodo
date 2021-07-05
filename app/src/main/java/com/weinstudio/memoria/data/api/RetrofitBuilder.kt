package com.weinstudio.memoria.data.api

import com.weinstudio.memoria.data.api.interceptor.HeaderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private const val BASE_URL = "https://d5dps3h13rv6902lp5c8.apigw.yandexcloud.net/"

    private var retrofit: Retrofit? = null
    private val client: OkHttpClient.Builder by lazy {
        OkHttpClient().newBuilder().apply {
            networkInterceptors().add(HeaderInterceptor())

            // Timeouts
            connectTimeout(1, TimeUnit.MINUTES)
            readTimeout(1, TimeUnit.MINUTES)
            writeTimeout(1, TimeUnit.MINUTES)
        }
    }

    private fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
        }
        return retrofit!!
    }

    val retrofitServices: RetrofitServices
        get() = getClient().create(RetrofitServices::class.java)
}
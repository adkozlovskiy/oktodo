package com.weinstudio.memoria.data.api.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    companion object {
        const val token = "Bearer 613873db791f4d899a560339317bd2ef"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()


        Log.d("TAG", "intercept: ${originalRequest.body()}")
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", token)
            .build()

        return chain.proceed(newRequest)
    }
}
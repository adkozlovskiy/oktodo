package com.weinstudio.oktodo.data.api

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    companion object {
        const val TOKEN = "Bearer 613873db791f4d899a560339317bd2ef"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", TOKEN)
            .build()

        return chain.proceed(newRequest)
    }
}
package com.weinstudio.oktodo.di

import com.weinstudio.oktodo.data.api.ProblemsService
import com.weinstudio.oktodo.data.api.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    fun provideRetrofitServices(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory

    ): ProblemsService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://d5dps3h13rv6902lp5c8.apigw.yandexcloud.net/")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()

        return retrofit.create()
    }

    @Provides
    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        val builder = OkHttpClient().newBuilder()

        builder.networkInterceptors().add(headerInterceptor)

        builder.readTimeout(1, TimeUnit.MINUTES)
        builder.writeTimeout(1, TimeUnit.MINUTES)
        builder.connectTimeout(1, TimeUnit.MINUTES)

        return builder.build()
    }

    @Provides
    fun provideHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}
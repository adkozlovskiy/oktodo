package com.weinstudio.oktodo.di

import com.google.gson.Gson
import com.weinstudio.oktodo.data.api.HeaderInterceptor
import com.weinstudio.oktodo.data.api.ProblemsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
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
    @Singleton
    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        val builder = OkHttpClient().newBuilder()

        builder.networkInterceptors().add(headerInterceptor)

        builder.readTimeout(1, TimeUnit.MINUTES)
        builder.writeTimeout(1, TimeUnit.MINUTES)
        builder.connectTimeout(1, TimeUnit.MINUTES)

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}
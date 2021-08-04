package com.weinstudio.oktodo.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.weinstudio.oktodo.util.connectivity.ConnectivityProviderImpl
import com.weinstudio.oktodo.util.connectivity.LegacyConnectivityProviderImpl
import com.weinstudio.oktodo.util.connectivity.base.BaseConnectivityProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ConnectivityModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(appContext: Application): ConnectivityManager {
        return appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideConnectivityProvider(
        appContext: Application,
        connectivityManager: ConnectivityManager
    ): BaseConnectivityProvider {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ConnectivityProviderImpl(connectivityManager)
        } else {
            LegacyConnectivityProviderImpl(appContext, connectivityManager)
        }
    }
}
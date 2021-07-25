package com.weinstudio.oktodo.util.connectivity.base

import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi

interface ConnectivityProvider {
    interface ConnectivityStateListener {
        fun onStateChange(state: NetworkState)
    }

    fun addListener(listener: ConnectivityStateListener)
    fun removeListener(listener: ConnectivityStateListener)

    fun getNetworkState(): NetworkState

    sealed class NetworkState {
        object NotConnectedState : NetworkState()

        sealed class ConnectedState(val hasInternet: Boolean) : NetworkState() {

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            data class Connected(val capabilities: NetworkCapabilities) : ConnectedState(
                capabilities.hasCapability(NET_CAPABILITY_INTERNET)
            )

            @Suppress("DEPRECATION")
            data class ConnectedLegacy(val networkInfo: NetworkInfo) : ConnectedState(
                networkInfo.isConnectedOrConnecting
            )
        }
    }
}
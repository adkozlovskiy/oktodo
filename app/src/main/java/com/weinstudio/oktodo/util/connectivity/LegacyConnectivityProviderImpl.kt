package com.weinstudio.oktodo.util.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.net.ConnectivityManager.EXTRA_NETWORK_INFO
import android.net.NetworkInfo
import com.weinstudio.oktodo.util.connectivity.base.BaseConnectivityProvider
import com.weinstudio.oktodo.util.connectivity.base.ConnectivityProvider

@Suppress("DEPRECATION")
class LegacyConnectivityProviderImpl(
    private val context: Context,
    private val cm: ConnectivityManager
) : BaseConnectivityProvider() {

    private val receiver = ConnectivityReceiver()

    override fun subscribe() {
        context.registerReceiver(receiver, IntentFilter(CONNECTIVITY_ACTION))
    }

    override fun unsubscribe() {
        context.unregisterReceiver(receiver)
    }

    override fun getNetworkState(): ConnectivityProvider.NetworkState {
        val activeNetworkInfo = cm.activeNetworkInfo
        return if (activeNetworkInfo != null) {
            ConnectivityProvider.NetworkState.ConnectedState.ConnectedLegacy(activeNetworkInfo)
        } else {
            ConnectivityProvider.NetworkState.NotConnectedState
        }
    }

    private inner class ConnectivityReceiver : BroadcastReceiver() {
        override fun onReceive(c: Context, intent: Intent) {
            val networkInfo = cm.activeNetworkInfo
            val fallbackNetworkInfo: NetworkInfo? = intent.getParcelableExtra(EXTRA_NETWORK_INFO)

            val state: ConnectivityProvider.NetworkState =
                if (networkInfo?.isConnectedOrConnecting == true) {
                    ConnectivityProvider.NetworkState.ConnectedState.ConnectedLegacy(networkInfo)
                } else if (networkInfo != null && fallbackNetworkInfo != null &&
                    networkInfo.isConnectedOrConnecting != fallbackNetworkInfo.isConnectedOrConnecting
                ) {
                    ConnectivityProvider.NetworkState.ConnectedState.ConnectedLegacy(
                        fallbackNetworkInfo
                    )
                } else {
                    val state = networkInfo ?: fallbackNetworkInfo
                    if (state != null) ConnectivityProvider.NetworkState.ConnectedState.ConnectedLegacy(
                        state
                    ) else ConnectivityProvider.NetworkState.NotConnectedState
                }
            dispatchChange(state)
        }
    }
}
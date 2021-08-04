package com.weinstudio.oktodo.util.connectivity.base

import android.os.Handler
import android.os.Looper

abstract class BaseConnectivityProvider : ConnectivityProvider {

    private val handler = Handler(Looper.getMainLooper())
    private val listeners = mutableSetOf<ConnectivityProvider.ConnectivityStateListener>()
    private var subscribed = false

    override fun addListener(listener: ConnectivityProvider.ConnectivityStateListener) {
        listeners.add(listener)
        listener.onStateChange(getNetworkState())
        verifySubscription()
    }

    override fun removeListener(listener: ConnectivityProvider.ConnectivityStateListener) {
        listeners.remove(listener)
        verifySubscription()
    }

    private fun verifySubscription() {
        if (!subscribed && listeners.isNotEmpty()) {
            subscribe()
            subscribed = true
        } else if (subscribed && listeners.isEmpty()) {
            unsubscribe()
            subscribed = false
        }
    }

    protected fun dispatchChange(state: ConnectivityProvider.NetworkState) {
        handler.post {
            for (listener in listeners) {
                listener.onStateChange(state)
            }
        }
    }

    protected abstract fun subscribe()
    protected abstract fun unsubscribe()
}
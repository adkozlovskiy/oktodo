package com.weinstudio.oktodo.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.weinstudio.oktodo.util.connectivity.base.ConnectivityProvider

fun Context.getDrawableCompat(resId: Int): Drawable {
    return AppCompatResources.getDrawable(this, resId)!!
}

fun Context.getColorCompat(resId: Int): Int {
    return ResourcesCompat.getColor(resources, resId, theme)
}

fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
    return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
}
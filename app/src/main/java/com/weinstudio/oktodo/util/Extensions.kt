package com.weinstudio.oktodo.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources

fun Context.getDrawableCompat(resId: Int): Drawable {
    return AppCompatResources.getDrawable(this, resId)!!
}
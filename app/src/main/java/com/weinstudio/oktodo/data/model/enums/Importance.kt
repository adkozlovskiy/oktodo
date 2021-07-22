package com.weinstudio.oktodo.data.model.enums

import com.google.gson.annotations.SerializedName

enum class Importance(val value: Int) {

    @SerializedName("low")
    LOW(0),

    @SerializedName("basic")
    BASIC(1),

    @SerializedName("important")
    IMPORTANT(2)

}
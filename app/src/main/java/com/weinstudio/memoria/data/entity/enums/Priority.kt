package com.weinstudio.memoria.data.entity.enums

import com.google.gson.annotations.SerializedName

enum class Priority(val value: Int) {

    @SerializedName("low")
    LOW(1),

    @SerializedName("basic")
    DEFAULT(2),

    @SerializedName("important")
    HIGH(3)

}
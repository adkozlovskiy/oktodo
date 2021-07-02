package com.weinstudio.memoria.data.entity.enums

import com.google.gson.annotations.SerializedName

enum class Priority {

    @SerializedName("low")
    LOW,

    @SerializedName("basic")
    DEFAULT,

    @SerializedName("important")
    HIGH

}
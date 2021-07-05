package com.weinstudio.memoria.data.entity.enums

import com.google.gson.annotations.SerializedName

enum class Importance() {

    @SerializedName("low")
    LOW,

    @SerializedName("basic")
    BASIC,

    @SerializedName("important")
    IMPORTANT

}
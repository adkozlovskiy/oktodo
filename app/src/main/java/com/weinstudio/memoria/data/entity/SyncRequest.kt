package com.weinstudio.memoria.data.entity

import com.google.gson.annotations.SerializedName

class SyncRequest(

    @SerializedName("deleted")
    val deleted: IntArray,

    @SerializedName("other")
    val other: List<Problem>

)
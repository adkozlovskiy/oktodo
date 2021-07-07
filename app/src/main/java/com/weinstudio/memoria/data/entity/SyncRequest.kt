package com.weinstudio.memoria.data.entity

import com.google.gson.annotations.SerializedName

class SyncRequest(

    @SerializedName("deleted")
    val deleted: List<String>,

    @SerializedName("other")
    val other: List<Problem>

)
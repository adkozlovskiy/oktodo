package com.weinstudio.memoria.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.weinstudio.memoria.util.enums.Priority

data class Problem(

    @Expose
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("text")
    val text: String,

    @Expose
    @SerializedName("deadline")
    val deadline: Long?,

    @Expose
    @SerializedName("priority")
    val priority: Priority,

    @Expose
    @SerializedName("done")
    var isDone: Boolean,

    @Expose
    @SerializedName("created_at")
    val created: Long?,

    @Expose
    @SerializedName("updated_at")
    val updated: Long?

) : ListItem
package com.weinstudio.memoria.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.weinstudio.memoria.data.ListItem
import com.weinstudio.memoria.data.entity.enums.Priority

@Entity(tableName = "problems")
data class Problem(

    @PrimaryKey
    @SerializedName(value = "id")
    val id: Int,

    @ColumnInfo(name = "text")
    @SerializedName(value = "text")
    val text: String,

    @ColumnInfo(name = "deadline")
    @SerializedName(value = "text")
    val deadline: Long?,

    @ColumnInfo(name = "priority")
    @SerializedName(value = "priority")
    val priority: Priority,

    @ColumnInfo(name = "done")
    @SerializedName(value = "done")
    val done: Boolean,

    @ColumnInfo(name = "created")
    @SerializedName("created_at")
    val created: Long?,

    @ColumnInfo(name = "updated")
    @SerializedName("updated_at")
    val updated: Long?

) : ListItem
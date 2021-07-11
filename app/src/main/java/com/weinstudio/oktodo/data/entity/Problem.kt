package com.weinstudio.oktodo.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.weinstudio.oktodo.data.ListItem
import com.weinstudio.oktodo.data.entity.enums.Importance

@Entity(tableName = "problems")
data class Problem(

    @PrimaryKey
    @SerializedName(value = "id")
    val id: String,

    @ColumnInfo(name = "text")
    @SerializedName(value = "text")
    var text: String?,

    @ColumnInfo(name = "deadline")
    @SerializedName(value = "deadline")
    var deadline: Long?,

    @ColumnInfo(name = "importance")
    @SerializedName(value = "importance")
    val importance: Importance,

    @ColumnInfo(name = "done")
    @SerializedName(value = "done")
    val done: Boolean,

    @ColumnInfo(name = "created")
    @SerializedName("created_at")
    val created: Long,

    @ColumnInfo(name = "updated")
    @SerializedName("updated_at")
    val updated: Long

) : ListItem {

    companion object {
        const val PROBLEM_EXTRA_TAG = "problem_extra"
    }
}
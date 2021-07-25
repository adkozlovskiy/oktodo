package com.weinstudio.oktodo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.weinstudio.oktodo.data.model.enums.Importance

@Entity(tableName = "problems")
data class Problem(

    @PrimaryKey
    @SerializedName(value = "id")
    val id: String = "",

    @SerializedName(value = "text")
    var text: String = "",

    @SerializedName(value = "deadline")
    var deadline: Long? = null,

    @SerializedName(value = "importance")
    val importance: Importance = Importance.BASIC,

    @SerializedName(value = "done")
    val done: Boolean = false,

    @SerializedName("created_at")
    val created: Long = 0,

    @SerializedName("updated_at")
    val updated: Long = 0

) : ListItem {

    fun hasDeadline(): Boolean {
        return deadline != null
    }

    fun hasCreated(): Boolean {
        return created != 0L
    }

    companion object {
        const val PROBLEM_EXTRA_TAG = "problem_extra"
    }
}
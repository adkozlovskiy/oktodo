package com.weinstudio.affari.data

import com.weinstudio.affari.data._enum.Priority

data class Problem(
    val id: Int,
    val title: String,
    val deadline: Long?,
    val priority: Priority?,
    val notifyDate: Long?,
    val isDone: Boolean
)
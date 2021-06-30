package com.weinstudio.memoria.data.entity

import com.weinstudio.memoria.util.enums.Priority

data class Problem(
    val id: Int,
    val title: String,
    val deadline: Long?,
    val priority: Priority,
    val notifyDate: Long?,
    var isDone: Boolean
) : ListItem
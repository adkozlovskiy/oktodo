package com.weinstudio.affari.data

import com.weinstudio.affari.data._enum.Priority
import java.util.*

data class Problem(
    val id: Int,
    val title: String,
    val desc: String,
    val deadline: Long?,
    val priority: Priority?
)
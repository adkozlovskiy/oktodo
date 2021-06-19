package com.weinstudio.affari.data

import java.util.*

data class Task(
    val title: String?,
    val desc: String?,
    val deadline: Date?,
    val priority: Priority?
)
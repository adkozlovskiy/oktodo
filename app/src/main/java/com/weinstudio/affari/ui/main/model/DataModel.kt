package com.weinstudio.affari.ui.main.model

import com.weinstudio.affari.data.Priority
import com.weinstudio.affari.data.Task

object DataModel {

    fun loadData(): MutableList<Task> {
        return mutableListOf(Task("Покормить кота", "2", null, Priority.HIGH_PRIORITY))
    }
}
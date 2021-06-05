package com.weinstudio.affari.ui.model

import com.weinstudio.affari.data.Task

object TasksModel {

    fun loadTasks(): List<Task> {
        return listOf(Task("1", "2"))
    }
}
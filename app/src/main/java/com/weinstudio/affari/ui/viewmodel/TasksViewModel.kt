package com.weinstudio.affari.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.affari.data.Task
import com.weinstudio.affari.ui.model.TasksModel

class TasksViewModel : ViewModel() {

    private val tasks: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>().also {
            TasksModel.loadTasks()
        }
    }

    fun getTasks(): LiveData<List<Task>> {
        return tasks
    }

    fun updateTasks() {
        tasks.value = TasksModel.loadTasks()
    }
}
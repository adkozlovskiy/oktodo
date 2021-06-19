package com.weinstudio.affari.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.affari.data.Task
import com.weinstudio.affari.ui.main.model.DataModel

class DataViewModel : ViewModel() {

    private val data: MutableLiveData<MutableList<Task>> by lazy {
        MutableLiveData<MutableList<Task>>().also {
            DataModel.loadData()
        }
    }

    fun getData(): LiveData<MutableList<Task>> = data

    fun updateData() {
        data.value = DataModel.loadData()
    }
}
package com.weinstudio.affari.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.affari.ui.main.model.DataModel

class DataViewModel : ViewModel() {

    private val data: MutableLiveData<List<Any>> by lazy {
        MutableLiveData<List<Any>>().also {
            DataModel.loadData()
        }
    }

    fun getData(): LiveData<List<Any>> = data

    fun updateData() {
        data.value = DataModel.loadData()
    }
}
package com.weinstudio.affari.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.affari.data.Problem
import com.weinstudio.affari.ui.main.model.DataModel

class DataViewModel : ViewModel() {

    private val data: MutableLiveData<List<Problem>> by lazy {
        MutableLiveData<List<Problem>>().also {
            DataModel.loadData()
        }
    }

    fun getData(): LiveData<List<Problem>> = data

    fun updateData() {
        data.value = DataModel.loadData()
    }
}
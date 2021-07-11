package com.weinstudio.oktodo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val isEyeEnabled by lazy {
        MutableLiveData<Boolean>().apply {
            value = true
        }
    }
}
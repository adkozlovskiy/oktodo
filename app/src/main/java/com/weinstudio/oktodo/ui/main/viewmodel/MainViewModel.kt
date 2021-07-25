package com.weinstudio.oktodo.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _eyeButtonEnabled: MutableLiveData<Boolean> = MutableLiveData(true)
    val eyeButtonEnabled: LiveData<Boolean> = _eyeButtonEnabled

    fun changeEyeButtonEnabled() {
        _eyeButtonEnabled.value = _eyeButtonEnabled.value!!.not()
    }

    fun isEyeButtonEnabled() = _eyeButtonEnabled.value!!

}
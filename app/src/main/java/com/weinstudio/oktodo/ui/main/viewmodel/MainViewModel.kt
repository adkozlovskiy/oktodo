package com.weinstudio.oktodo.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _eyeButtonEnabledData: MutableLiveData<Boolean> = MutableLiveData(true)

    val eyeButtonEnabledData: LiveData<Boolean> = _eyeButtonEnabledData

    fun changeEyeButtonEnabled() {
        _eyeButtonEnabledData.value = _eyeButtonEnabledData.value!!.not()
    }

    fun isEyeButtonEnabled() = _eyeButtonEnabledData.value!!

}
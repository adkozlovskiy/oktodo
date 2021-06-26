package com.weinstudio.affari.ui.create.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.affari.data._enum.Priority
import java.util.*

class CreateViewModel : ViewModel() {

    val calendar: Calendar by lazy { Calendar.getInstance() }

    val datetimeData by lazy {
        MutableLiveData<String>()
    }

    val tags: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    val deadline: MutableLiveData<Date> by lazy {
        MutableLiveData<Date>()
    }

    val priority: MutableLiveData<Priority> by lazy {
        MutableLiveData<Priority>()
    }

    val oneTimeNotifications: MutableLiveData<Date> by lazy {
        MutableLiveData<Date>()
    }

    val regularNotifications: MutableLiveData<Date> by lazy {
        MutableLiveData<Date>()
    }
}
package com.weinstudio.memoria.ui.create.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.memoria.data.entity.enums.Importance
import java.util.*

class CreateViewModel : ViewModel() {

    // Calendar instance.
    val calendar: Calendar by lazy { Calendar.getInstance() }

    // Deadline card inner text.
    val deadlineText by lazy { MutableLiveData<String>() }

    val priorityProp by lazy {
        MutableLiveData<Importance>()
            .apply {
                value = Importance.BASIC
            }
    }

    // TODO: notifications
    val notificationsProp by lazy { MutableLiveData<Date>() }

}
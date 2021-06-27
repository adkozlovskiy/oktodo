package com.weinstudio.memoria.ui.create.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.memoria.util.enums.Priority
import java.util.*

class CreateViewModel : ViewModel() {

    // Calendar instance.
    val calendar: Calendar by lazy { Calendar.getInstance() }

    // Deadline card inner text.
    val deadlineText by lazy { MutableLiveData<String>() }

    val priorityProp by lazy {
        MutableLiveData<Priority>()
            .apply {
                value = Priority.DEFAULT
            }
    }

    // TODO: notifications
    val notificationsProp by lazy { MutableLiveData<Date>() }

    // TODO: tags
    val tagsData by lazy { MutableLiveData<List<String>>() }

}
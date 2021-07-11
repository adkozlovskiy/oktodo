package com.weinstudio.oktodo.ui.edit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weinstudio.oktodo.data.repository.ProblemsRepository

class EditViewModelFactory(private val repository: ProblemsRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class.")
    }
}
package com.weinstudio.memoria.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weinstudio.memoria.data.repository.ProblemRepository

class ProblemsViewModelFactory(private val repository: ProblemRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProblemsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProblemsViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class.")
    }
}
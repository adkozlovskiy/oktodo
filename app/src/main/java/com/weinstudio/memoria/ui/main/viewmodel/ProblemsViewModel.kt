package com.weinstudio.memoria.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.repository.ProblemRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ProblemsViewModel(
    private val repository: ProblemRepository

) : ViewModel() {

    private val filterFlow = MutableStateFlow(false)

    @ExperimentalCoroutinesApi
    val allProblems = filterFlow.flatMapLatest {
        repository.getProblems(it)

    }.asLiveData()

    val doneCount = repository.getCountFlow(true).asLiveData()

    fun changeStatus(problem: Problem, status: Boolean) {
        val id = problem.id

        if (id != null) {
            repository.changeStatus(id, status)

        } else {
            throw java.lang.IllegalArgumentException("Problem id must not be null.")
        }
    }

    fun deleteProblem(problem: Problem) = viewModelScope.launch {
        repository.deleteProblem(problem)
    }

    @ExperimentalCoroutinesApi
    fun setFilterFlag(flag: Boolean) {
        filterFlow.value = flag
    }
}




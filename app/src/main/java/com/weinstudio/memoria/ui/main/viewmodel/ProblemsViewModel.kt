package com.weinstudio.memoria.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.repository.ProblemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ProblemsViewModel(
    private val repository: ProblemsRepository

) : ViewModel() {

    private val filterFlow = MutableStateFlow(false)

    @ExperimentalCoroutinesApi
    val allProblems = filterFlow.flatMapLatest {
        repository.getProblems(it)

    }.asLiveData()

    val doneCount = repository.getCountFlow(true).asLiveData()

    fun changeDoneFlag(problem: Problem, done: Boolean) = viewModelScope
        .launch(Dispatchers.IO) {
            val newProblem = problem.copy(done = done)
            repository.updateProblem(newProblem)
        }

    fun deleteProblem(problem: Problem) = viewModelScope
        .launch(Dispatchers.IO) {
            repository.deleteProblem(problem)
        }

    fun refreshProblems() = viewModelScope
        .launch {
            repository.refreshProblems()
        }

    @ExperimentalCoroutinesApi
    fun setFilterFlag(flag: Boolean) {
        filterFlow.value = flag
    }
}




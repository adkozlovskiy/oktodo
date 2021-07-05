package com.weinstudio.memoria.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.repository.ProblemsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class ProblemsViewModel(
    private val repository: ProblemsRepository

) : ViewModel() {

    private val filterFlow = MutableStateFlow(false)

    @ExperimentalCoroutinesApi
    val allProblems = filterFlow.flatMapLatest {
        repository.getProblems(it)

    }.asLiveData()

    val doneCount = repository.getCountFlow(true).asLiveData()

    fun changeDone(problem: Problem, done: Boolean) {
        val newProblem = problem.copy(done = done)
        repository.updateProblem(newProblem)
    }

    fun deleteProblem(problem: Problem) {
        repository.deleteProblem(problem)
    }

    @ExperimentalCoroutinesApi
    fun setFilterFlag(flag: Boolean) {
        filterFlow.value = flag
    }
}




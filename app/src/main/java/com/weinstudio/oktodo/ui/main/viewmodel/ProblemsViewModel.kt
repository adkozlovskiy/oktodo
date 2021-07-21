package com.weinstudio.oktodo.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.weinstudio.oktodo.data.model.Problem
import com.weinstudio.oktodo.data.repository.ProblemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProblemsViewModel @Inject constructor(
    private val repository: ProblemsRepository

) : ViewModel() {

    private val filteredFlow = MutableStateFlow(false)

    @ExperimentalCoroutinesApi
    val allProblems = filteredFlow.flatMapLatest { filtered ->
        repository.getProblemsFlow(filtered)
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

    @ExperimentalCoroutinesApi
    fun setFilterFlag(flag: Boolean) {
        filteredFlow.value = flag
    }

    fun refreshProblems() {
        repository.enqueueRefreshProblems()
    }
}
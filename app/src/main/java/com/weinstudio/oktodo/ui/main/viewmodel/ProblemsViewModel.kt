package com.weinstudio.oktodo.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.weinstudio.oktodo.data.ProblemsRepository
import com.weinstudio.oktodo.data.model.Problem
import com.weinstudio.oktodo.util.WorkerEnquirer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProblemsViewModel @Inject constructor(
    private val repository: ProblemsRepository,
    private val workerEnquirer: WorkerEnquirer

) : ViewModel() {

    private val filteredFlow = MutableStateFlow(false)

    @ExperimentalCoroutinesApi
    val allProblems = filteredFlow.flatMapLatest { filtered ->
        repository.getProblemsFlow(filtered)
    }.asLiveData()

    val doneCount = repository.getCountFlow(true).asLiveData()

    fun changeDoneFlag(problem: Problem, done: Boolean) = viewModelScope.launch {
        val entryProblem = problem.copy(
            done = done
        )

        repository.updateProblem(entryProblem)
        workerEnquirer.enqueueUpdate(entryProblem)
    }

    fun deleteProblem(problem: Problem) = viewModelScope.launch {
        repository.deleteProblem(problem)
        workerEnquirer.enqueueDelete(problem)
    }

    @ExperimentalCoroutinesApi
    fun setFilterFlag(flag: Boolean) {
        filteredFlow.value = flag
    }

    fun refreshProblems() {
        workerEnquirer.enqueueRefresh()
    }
}
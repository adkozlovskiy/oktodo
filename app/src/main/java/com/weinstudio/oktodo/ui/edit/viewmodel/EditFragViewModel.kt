package com.weinstudio.oktodo.ui.edit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weinstudio.oktodo.data.ProblemsRepository
import com.weinstudio.oktodo.data.model.Problem
import com.weinstudio.oktodo.data.model.enums.Importance
import com.weinstudio.oktodo.util.WorkerEnquirer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditFragViewModel @Inject constructor(
    private val repository: ProblemsRepository,
    private val workerEnquirer: WorkerEnquirer

) : ViewModel() {

    private val _problemProperties = MutableLiveData<Problem>()
    val problemProperties: LiveData<Problem> = _problemProperties

    fun setProblemValue(problem: Problem) {
        _problemProperties.value = problem
    }

    fun getProblemValue(): Problem {
        return _problemProperties.value!!
    }

    val deadlineCalendar: Calendar = Calendar.getInstance()

    fun initDeadlineCalendar(initMillis: Long?) {
        if (initMillis != null) {
            deadlineCalendar.timeInMillis = initMillis * 1000
        }
    }

    fun setProblemDeadline(deadlineMillis: Long?) {
        val oldProblem = getProblemValue()

        val newProblem = oldProblem.copy(
            deadline = if (deadlineMillis == null) null else (deadlineMillis / 1000)
        )

        setProblemValue(newProblem)
    }

    fun setProblemImportance(ordinal: Int) {
        val oldProblem = getProblemValue()
        val importance = when (ordinal) {
            0 -> Importance.LOW
            1 -> Importance.BASIC
            2 -> Importance.IMPORTANT

            else -> throw IllegalStateException()
        }

        val newProblem = oldProblem.copy(
            importance = importance
        )

        setProblemValue(newProblem)
    }

    fun insertProblem(problem: Problem) = viewModelScope.launch {
        val currentMillis = Calendar.getInstance().timeInMillis
        val currentMillisCropped = currentMillis / 1000

        val entryProblem = problem.copy(
            id = "$currentMillis",
            created = currentMillisCropped,
            updated = currentMillisCropped
        )

        repository.insertProblem(entryProblem)
        workerEnquirer.enqueueInsert(entryProblem)
    }

    fun updateProblem(problem: Problem) = viewModelScope.launch {
        val currentMillis = Calendar.getInstance().timeInMillis
        val currentMillisCropped = currentMillis / 1000

        val entryProblem = problem.copy(
            updated = currentMillisCropped
        )

        repository.updateProblem(entryProblem)
        workerEnquirer.enqueueUpdate(entryProblem)
    }
}
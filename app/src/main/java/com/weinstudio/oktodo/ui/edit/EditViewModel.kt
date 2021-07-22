package com.weinstudio.oktodo.ui.edit

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
class EditViewModel @Inject constructor(
    private val repository: ProblemsRepository,
    private val workerEnquirer: WorkerEnquirer

) : ViewModel() {

    // Calendar instance
    val deadlineCalendar: Calendar by lazy {
        Calendar.getInstance().apply {
            val deadline = problemData.value?.deadline
            if (deadline != null) {
                timeInMillis = deadline * 1000
            }
        }
    }

    val problemData: MutableLiveData<Problem> by lazy {
        MutableLiveData<Problem>().apply {

            // Default problem template
            value = Problem(
                id = "",
                text = null,
                deadline = null,
                importance = Importance.BASIC,
                done = false,
                created = -1,
                updated = -1
            )
        }
    }

    val deadlineSwitchChecked by lazy {
        MutableLiveData(problemData.value?.deadline != null)
    }

    fun setDeadlineSwitchChecked(isChecked: Boolean) {
        deadlineSwitchChecked.value = isChecked
    }

    fun updateDeadlineProp() {
        val oldProblem = problemData.value!!

        val newProblem = oldProblem.copy(
            deadline = deadlineCalendar.timeInMillis / 1000
        )

        problemData.value = newProblem
    }

    fun setImportanceProp(checked: Int) {
        val oldProblem = problemData.value!!
        val importance = when (checked) {
            0 -> Importance.LOW
            1 -> Importance.BASIC
            2 -> Importance.IMPORTANT

            else -> throw IllegalStateException()
        }

        val newProblem = oldProblem.copy(
            importance = importance
        )

        problemData.value = newProblem
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
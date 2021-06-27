package com.weinstudio.memoria.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.repository.ProblemsRepository

class MainViewModel : ViewModel() {

    val problemsLiveData = MutableLiveData<MutableList<Problem>>()

    fun updateData() {
        problemsList.clear()
        problemsList.addAll(
            ProblemsRepository.problemsList
                .sortedByDescending { it.priority }
        )
        problemsLiveData.value = problemsList
    }

    var problemsList = mutableListOf<Problem>()

    fun setProblemDoneFlag(p: Problem, f: Boolean) {
        ProblemsRepository.setProblemDoneFlag(p, f)
        updateData()
    }

    fun insertProblem(pos: Int, p: Problem) {
        ProblemsRepository.insertProblem(pos, p)
        updateData()
    }

    fun removeProblem(p: Problem) {
        ProblemsRepository.removeProblem(p)
        updateData()
    }

    fun filterProblems() {
        problemsList = problemsList.filter { !it.isDone }.toMutableList()
        problemsLiveData.value = problemsList
    }

}
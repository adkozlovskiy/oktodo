package com.weinstudio.memoria.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.repository.ProblemsRepository

class MainViewModel : ViewModel() {

    val problemsLiveData = MutableLiveData<MutableList<Problem>>()

    val countLiveDate = MutableLiveData<Int>().apply { value = 0 }

    fun updateData() {
        problemsList.clear()

        val newProblems = ProblemsRepository.problemsList
            .sortedByDescending { it.priority }

        countLiveDate.value = newProblems.count { it.done }

        problemsList.addAll(newProblems)
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
        problemsList = problemsList.filter { !it.done }.toMutableList()
        problemsLiveData.value = problemsList
    }

}
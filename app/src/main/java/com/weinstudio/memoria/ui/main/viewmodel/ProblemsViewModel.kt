package com.weinstudio.memoria.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.repository.ProblemsRepository

class ProblemsViewModel : ViewModel() {

    private var problemsList = mutableListOf<Problem>()

    val problemsLiveData = MutableLiveData<MutableList<Problem>>()

    fun insertProblem(pos: Int, p: Problem) {
        ProblemsRepository.insertProblem(pos, p)
        getDataFromModel()
    }

    fun removeProblem(p: Problem) {
        ProblemsRepository.removeProblem(p)
        getDataFromModel()
    }

    fun setProblemDoneFlag(p: Problem, f: Boolean) {
        ProblemsRepository.setProblemDoneFlag(p, f)
        getDataFromModel()
    }

    fun filterProblems() {
        problemsList = problemsList.filter {
            !it.isDone
        }.toMutableList()

        problemsLiveData.value = problemsList
    }

    fun getDataFromModel() {
        problemsList.clear()
        problemsList.addAll(
            ProblemsRepository.problemsList
                .sortedWith(compareBy(Problem::deadline, Problem::priority))
        )
        problemsLiveData.value = problemsList
    }
}
package com.weinstudio.memoria.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.repository.ProblemsRepository

class ProblemsViewModel : ViewModel() {

    private var problemsList = arrayListOf<Problem>()

    val problemsLiveData = MutableLiveData<ArrayList<Problem>>()

    fun insertProblem(pos: Int, p: Problem) {
        ProblemsRepository.insertProblem(pos, p)
        getDataFromModel()
    }

    fun removeProblem(pos: Int): Problem {
        val removed = ProblemsRepository.removeProblem(pos)
        getDataFromModel()
        return removed
    }

    fun getDataFromModel() {
        problemsList.clear()
        problemsList.addAll(ProblemsRepository.problemsList)
        problemsLiveData.value = problemsList
    }
}
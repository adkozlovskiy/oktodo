package com.weinstudio.affari.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.affari.data.Problem
import com.weinstudio.affari.model.ProblemsModel

class ProblemsViewModel : ViewModel() {

    private var problemsList = arrayListOf<Problem>()

    val problemsLiveData = MutableLiveData<ArrayList<Problem>>()

    fun insertProblem(pos: Int, p: Problem) {
        ProblemsModel.insertProblem(pos, p)
        getDataFromModel()
    }

    fun removeProblem(pos: Int): Problem {
        val removed = ProblemsModel.removeProblem(pos)
        getDataFromModel()
        return removed
    }

    fun getDataFromModel() {
        problemsList.clear()
        problemsList.addAll(ProblemsModel.problemsList)
        problemsLiveData.value = problemsList
    }
}
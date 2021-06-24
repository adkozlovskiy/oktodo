package com.weinstudio.affari.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weinstudio.affari.data.Problem
import com.weinstudio.affari.ui.main.model.ProblemsModel

class ProblemsViewModel : ViewModel() {

    private var problemsList = ArrayList<Problem>()

    val problemsLiveData = MutableLiveData<ArrayList<Problem>>()

    init {
        loadDataFromServer()
    }

    fun insertProblem(pos: Int, p: Problem) {
        problemsList.add(pos, p)
        problemsLiveData.value = problemsList
    }

    fun removeProblem(pos: Int): Problem {
        val removed = problemsList.removeAt(pos)
        problemsLiveData.value = problemsList
        return removed
    }

    private fun loadDataFromServer() {
        problemsList = ProblemsModel.loadData()
        problemsLiveData.value = problemsList
    }
}
package com.weinstudio.affari.model

import com.weinstudio.affari.data.Problem
import com.weinstudio.affari.data._enum.Priority

object ProblemsModel {

    val problemsList by lazy {
        loadData()
    }

    fun addProblem(p: Problem) {
        problemsList.add(0, p)
    }

    fun removeProblem(pos: Int): Problem {
        return problemsList.removeAt(pos)
    }

    fun insertProblem(pos: Int, p: Problem) {
        problemsList.add(pos, p)
    }

    private fun loadData(): ArrayList<Problem> {
        return arrayListOf(
            Problem(
                1,
                "Покормить кота",
                1624552533145,
                Priority.HIGH_PRIORITY, null, false
            ),
            Problem(
                2,
                "Помыть посуду",
                1624552533145,
                Priority.HIGH_PRIORITY, null, false
            ),
            Problem(
                3,
                "Подготовиться к экзамену по английскому",
                null,
                Priority.HIGH_PRIORITY, null, false
            ),
            Problem(
                4,
                "Купить подарок котику",
                null,
                Priority.LOW_PRIORITY, null, false
            )
        )
    }
}
package com.weinstudio.affari.model

import com.weinstudio.affari.data.Problem
import com.weinstudio.affari.data._enum.Priority

object ProblemsModel {

    val problems by lazy {
        loadData()
    }

    fun addProblem(p: Problem) {
        problems.add(p)
    }

    fun removeProblem(pos: Int): Problem {
        return problems.removeAt(pos)
    }

    fun insertProblem(pos: Int, p: Problem) {
        problems.add(pos, p)
    }

    private fun loadData(): ArrayList<Problem> {
        return arrayListOf(
            Problem(
                1,
                "Покормить кота",
                "2",
                1624552533145,
                Priority.HIGH_PRIORITY
            ),
            Problem(
                2,
                "Помыть посуду",
                "2",
                1624552533145,
                Priority.HIGH_PRIORITY
            ),
            Problem(
                3,
                "Подготовиться к экзамену по английскому",
                "2",
                null,
                Priority.HIGH_PRIORITY
            ),
            Problem(
                4,
                "Купить подарок котику",
                "2",
                null,
                Priority.LOW_PRIORITY
            )
        )
    }
}
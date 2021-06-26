package com.weinstudio.memoria.data.repository

import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.util.enums.Priority

object ProblemsRepository {

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
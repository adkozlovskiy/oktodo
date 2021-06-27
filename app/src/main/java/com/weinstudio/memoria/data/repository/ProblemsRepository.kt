package com.weinstudio.memoria.data.repository

import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.util.enums.Priority

// Repository and local storage :)
object ProblemsRepository {

    val problemsList by lazy {
        loadData()
    }

    fun removeProblem(p: Problem) {
        problemsList.remove(p)
    }

    fun insertProblem(pos: Int, p: Problem) {
        problemsList.add(pos, p)
    }

    fun setProblemDoneFlag(p: Problem, f: Boolean) {
        val index = problemsList.indexOf(p)
        p.isDone = f
        problemsList[index] = p
    }

    // Getting data from network
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
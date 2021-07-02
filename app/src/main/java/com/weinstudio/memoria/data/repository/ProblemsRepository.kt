package com.weinstudio.memoria.data.repository

import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.entity.enums.Priority
import java.util.*
import kotlin.collections.ArrayList

// Repository and local storage :)
object ProblemsRepository {

    val problemsList by lazy {
        loadData()
    }

    fun addProblem(p: Problem) {
        problemsList.add(p)
    }

    fun removeProblem(p: Problem) {
        problemsList.remove(p)
    }

    fun insertProblem(pos: Int, p: Problem) {
        problemsList.add(pos, p)
    }

    fun setProblemDoneFlag(p: Problem, f: Boolean) {
        val index = problemsList.indexOf(p)
        val newProblem = p.copy(done = f)
        problemsList[index] = newProblem
    }

    // Getting data from network
    private fun loadData(): ArrayList<Problem> {
        return arrayListOf(
            Problem(
                1,
                "Покормить кота",
                1624552533145,
                Priority.HIGH, false, Date().time, Date().time
            ),
            Problem(
                2,
                "Помыть посуду",
                1624552533146,
                Priority.HIGH, false, Date().time, Date().time
            ),
            Problem(
                3,
                "Подготовиться к экзамену по английскому",
                null,
                Priority.HIGH, false, Date().time, Date().time
            ),
            Problem(
                4,
                "Купить подарок котику",
                null,
                Priority.HIGH, false, Date().time, Date().time
            )
        )
    }
}
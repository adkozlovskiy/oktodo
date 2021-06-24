package com.weinstudio.affari.ui.main.model

import com.weinstudio.affari.data.Problem
import com.weinstudio.affari.data._enum.Priority

object ProblemsModel {

    fun loadData(): ArrayList<Problem> {
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
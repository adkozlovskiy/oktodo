package com.weinstudio.affari.ui.main.model

import com.weinstudio.affari.data.Problem
import com.weinstudio.affari.data._enum.Priority

object DataModel {

    fun loadData(): List<Problem> {
        return listOf(
            Problem(
                1,
                "Покормить кота",
                "2",
                null,
                Priority.HIGH_PRIORITY
            ),
            Problem(
                2,
                "Покормить кота",
                "2",
                null,
                Priority.HIGH_PRIORITY
            )
        )
    }
}
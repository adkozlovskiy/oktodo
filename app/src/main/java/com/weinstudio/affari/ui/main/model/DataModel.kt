package com.weinstudio.affari.ui.main.model

import com.weinstudio.affari.data.Category
import com.weinstudio.affari.data.Task

object DataModel {

    fun loadData(): MutableList<Any> {
        return mutableListOf(Category("Сегодня"), Task("Покормить кота", "2"), Task("Искупать кота", "2"))
    }
}
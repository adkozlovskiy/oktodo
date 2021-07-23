package com.weinstudio.oktodo.ui.main.view

import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import pages.EditPage
import pages.MainPage

class ProblemAddingTest {

    @get:Rule
    val activityEditRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun should_add_new_problem() {
        MainPage.obtain()
            .clickCreateButton()

        EditPage.obtain()
            .assertOn()
            .inputTitle(TEST_TITLE)
            .selectImportance(TEST_IMPORTANCE)
            .toggleDeadlineSwitch()
            .clickOkButton()

        MainPage.obtain()
            .checkRecyclerViewHasNewItem(TEST_TITLE)
    }

    @Test
    fun should_show_error_text_when_empty_title() {
        MainPage.obtain()
            .clickCreateButton()

        EditPage.obtain()
            .assertOn()
            .clickOkButton()
            .checkErrorText(EMPTY_TITLE_ERROR_TEXT)
    }

    companion object {
        const val TEST_TITLE = "Уникальное название для проверки"
        const val TEST_IMPORTANCE = "Низкий"
        const val EMPTY_TITLE_ERROR_TEXT = "Почему бы вам не сделать что-то?"
    }
}

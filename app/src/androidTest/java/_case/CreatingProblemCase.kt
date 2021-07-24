package _case

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.weinstudio.oktodo.ui.main.view.MainActivity
import org.junit.Rule
import org.junit.Test
import page.EditPage
import page.MainPage

class CreatingProblemCase {

    @get:Rule
    val activityEditRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun shouldCreateProblem() {
        MainPage.obtain()
            .assertOn()
            .clickCreateButton()

        EditPage.obtain()
            .assertOn()
            .inputTitle(TEST_TITLE)
            .selectImportance(TEST_IMPORTANCE)
            .clickOkButton()

        MainPage.obtain()
            .assertOn()
            .scrollToProblemInRecycler(TEST_TITLE)
    }

    @Test
    fun shouldShowErrorText_WhenEmptyTitle() {
        MainPage.obtain()
            .assertOn()
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

package _case

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.weinstudio.oktodo.ui.main.view.MainActivity
import exception.ProblemNotFoundException
import org.junit.Rule
import org.junit.Test
import page.EditPage
import page.MainPage

class SwitchingEyeButtonCase {

    @get:Rule
    val mainActivityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test(expected = ProblemNotFoundException::class)
    fun shouldNotFoundCreatedProblem_WithDoneFlag() {
        MainPage.obtain()
            .assertOn()
            .clickCreateButton()

        EditPage.obtain()
            .assertOn()
            .inputTitle(PROBLEM_TITLE_TO_BE_TESTED)
            .clickOkButton()

        MainPage.obtain()
            .assertOn()
            .doneProblem(PROBLEM_TITLE_TO_BE_TESTED)

        MainPage.obtain()
            .assertOn()
            .clickEyeButton()
            .scrollToProblemInRecycler(PROBLEM_TITLE_TO_BE_TESTED) // throws ProblemNotFoundException
    }

    companion object {
        const val PROBLEM_TITLE_TO_BE_TESTED = "eye button test"
    }
}
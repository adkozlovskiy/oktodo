package _case

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.weinstudio.oktodo.ui.main.view.MainActivity
import exception.ProblemNotFoundException
import org.junit.Rule
import org.junit.Test
import page.MainPage

class DeletingProblemCase {

    @get:Rule
    val activityMainRule = ActivityScenarioRule(MainActivity::class.java)

    @Test(expected = ProblemNotFoundException::class)
    fun shouldFindExistingProblem() {
        MainPage.obtain()
            .assertOn()
            .deleteProblem(TASK_TO_BE_DELETED_TITLE)
            .scrollToProblemInRecycler(TASK_TO_BE_DELETED_TITLE) // throws ProblemNotFoundException
    }

    companion object {
        const val TASK_TO_BE_DELETED_TITLE = "eye button test3"
    }
}
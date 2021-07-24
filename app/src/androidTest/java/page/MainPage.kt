package page

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.ui.main.adapter.fingerprint.ProblemFingerprint
import exception.ProblemNotFoundException
import org.hamcrest.Matchers.not

class MainPage : BasePage() {

    companion object {
        fun obtain(): MainPage = MainPage()
    }

    override fun assertOn(): MainPage {
        return this
    }

    fun clickCreateButton(): MainPage {
        onView(withId(R.id.fab_create)).perform(click())
        return this
    }

    @Throws(ProblemNotFoundException::class)
    fun scrollToProblemInRecycler(title: String): MainPage {
        try {
            onView(withId(R.id.recycler)).perform(
                RecyclerViewActions.scrollTo<ProblemFingerprint.ProblemViewHolder>(
                    hasDescendant(
                        withText(title)
                    )
                )
            )
        } catch (ex: PerformException) {
            throw ProblemNotFoundException("Problem not found")
        }

        return this
    }

    fun deleteProblem(title: String): MainPage {
        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItem<ProblemFingerprint.ProblemViewHolder>(
                hasDescendant(
                    withText(title)
                ),
                getSwipeLeftAction()
            )
        )

        return this
    }

    fun doneProblem(title: String): MainPage {
        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItem<ProblemFingerprint.ProblemViewHolder>(
                hasDescendant(
                    withText(title)
                ),
                getSwipeRightAction()
            )
        )

        return this
    }

    private fun getSwipeLeftAction(): ViewAction {
        return ViewActions.swipeLeft()
    }

    private fun getSwipeRightAction(): ViewAction {
        return ViewActions.swipeRight()
    }

    fun clickEyeButton(): MainPage {
        onView(withId(R.id.action_eye)).perform(click())
        return this
    }
}
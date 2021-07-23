package pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.ui.main.adapter.fingerprint.ProblemFingerprint

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

    fun checkRecyclerViewHasNewItem(title: String): MainPage {
        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.scrollTo<ProblemFingerprint.ProblemViewHolder>(
                hasDescendant(
                    withText(title)
                )
            )
        )
        return this
    }
}
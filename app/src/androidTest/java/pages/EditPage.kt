package pages

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.textfield.TextInputLayout
import com.weinstudio.oktodo.R


class EditPage : BasePage() {

    companion object {
        fun obtain(): EditPage = EditPage()
    }

    override fun assertOn(): EditPage {
        return this
    }

    fun inputTitle(title: String): EditPage {
        onView(withId(R.id.et_title)).perform(replaceText(title))
        return this
    }

    fun selectImportance(importance: String): EditPage {
        onView(withId(R.id.cv_priority)).perform(click())
        onView(withText(importance)).inRoot(isDialog()).check(matches(isDisplayed()))
            .perform(click())
        onView(withText("OK")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
        return this
    }

    fun toggleDeadlineSwitch(): EditPage {
        onView(withId(R.id.switch_deadline)).perform(click())
        return this
    }

    fun checkErrorText(errorText: String): EditPage {
        onView(withId(R.id.ti_title)).check(matches(has_text_input_layout_error_text(errorText)))
        return this
    }

    fun clickOkButton(): EditPage {
        onView(withId(R.id.action_create)).perform(click())
        return this
    }

    private fun has_text_input_layout_error_text(expectedErrorText: String): org.hamcrest.Matcher<View> {
        return object : org.hamcrest.TypeSafeMatcher<View>() {
            override fun matchesSafely(view: View): Boolean {
                if (view !is TextInputLayout) {
                    return false
                }
                val error = (view as TextInputLayout).error ?: return false
                val hint = error.toString()
                return expectedErrorText == hint
            }

            override fun describeTo(description: org.hamcrest.Description?) {}
        }
    }
}
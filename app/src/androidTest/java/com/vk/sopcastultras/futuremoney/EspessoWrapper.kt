package com.vk.sopcastultras.futuremoney

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import io.qameta.allure.kotlin.Allure
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun myStep(name: String, action: () -> Unit) = Allure.step(name) { action.invoke() }

fun Matcher<View>.click() {
    myStep("Нажали на '$this'") {
        Espresso.onView(this).perform(ViewActions.click())
    }

}

fun Matcher<View>.typeText(text: String) {
    myStep("Ввели $text в '$this'") {
        Espresso.onView(this)
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText(text))
    }
}

fun Matcher<View>.selectInSpinner(value: String) {
    myStep("Выбрали $value в '$this'") {
        Espresso.onView(this).perform(ViewActions.click())
        Espresso.onData(
            CoreMatchers.allOf(
                CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)),
                CoreMatchers.`is`(value)
            )
        ).perform(ViewActions.click())
        Espresso.onView(this).check(
            ViewAssertions.matches(
                ViewMatchers.withSpinnerText(
                    CoreMatchers.containsString(value)
                )
            )
        )
    }
}

fun Matcher<View>.checkIsDisplayed() {
    myStep("Проверили видимость '$this'") {
        Espresso.onView(this).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}

fun Matcher<View>.checkText(expected: String) {
    myStep("Проверили наличие текста '$expected' для '$this'") {
        if (this.getText().contains(expected)) {
            throw AssertionError("Отличается текст") // TODO подробности мб
        }
    }
}

fun Matcher<View>.getText(): String {
    var text = String()
    Espresso.onView(this).perform(object : ViewAction {
        override fun getConstraints() = ViewMatchers.isAssignableFrom(TextView::class.java)
        override fun getDescription() = "Text of the view"
        override fun perform(uiController: UiController, view: View) {
            val tv = view as TextView
            text = tv.text.toString()
        }
    })

    return text
}

fun withIndex(matcher: Matcher<View>, index: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        var currentIndex = 0
        override fun describeTo(description: Description) {
            description.appendText("with index: ")
            description.appendValue(index)
            matcher.describeTo(description)
        }

        override fun matchesSafely(view: View): Boolean {
            return matcher.matches(view) && currentIndex++ == index
        }
    }
}

package com.vk.sopcastultras.futuremoney

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import io.qameta.allure.kotlin.Allure
import org.hamcrest.Matcher

abstract class BasePage<T> {

    /* Временно здесь чтобы меньше импортировать */

    protected fun Matcher<View>.click() {
        myStep("Нажали на '$this'") {
            onView(this).perform(ViewActions.click())
        }

    }

    protected fun Matcher<View>.typeText(text: String) {
        myStep("Ввели $text в '$this'") {
            onView(this)
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText(text))
        }
    }

    protected fun Matcher<View>.checkVisible() {
        myStep("Проверили видимость '$this'") {
            onView(this).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    protected fun Matcher<View>.checkText(expected: String) {
        myStep("Проверили наличие текста '$expected' для '$this'") {
            if (this.getText().contains(expected)) {
                throw AssertionError("Отличается текст")
            }
        }
    }

    private fun Matcher<View>.getText(): String {
        var text = String()
        onView(this).perform(object : ViewAction {
            override fun getConstraints() = isAssignableFrom(TextView::class.java)
            override fun getDescription() = "Text of the view"
            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                text = tv.text.toString()
            }
        })

        return text
    }

    private fun myStep(name: String, action: () -> Unit) = Allure.step(name) { action.invoke() }

    // magic from internet plus my idea
    operator fun <R> invoke(block: T.() -> R) {
        myStep(this.javaClass.simpleName) {
            block.invoke(this as T)
        }
    }
}

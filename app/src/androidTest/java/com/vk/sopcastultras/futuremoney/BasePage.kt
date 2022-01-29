package com.vk.sopcastultras.futuremoney

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import io.qameta.allure.kotlin.Allure
import org.hamcrest.Matcher

abstract class BasePage {

    /* Временно здесь чтобы меньше импортировать */

    protected fun Matcher<View>.click() {
        myStep("Нажали на '$this'") {
            onView(this).perform(ViewActions.click())
        }

    }

    protected fun Matcher<View>.typeText(text: String) {
        myStep("Ввели $text в '$this'") {
            onView(this).perform(ViewActions.typeText(text))
        }
    }

    protected fun Matcher<View>.checkVisible() {
        myStep("Проверили видимость '$this'") {
            onView(this).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    private fun myStep(name: String, action: () -> Unit) = Allure.step(name) { action.invoke() }
}

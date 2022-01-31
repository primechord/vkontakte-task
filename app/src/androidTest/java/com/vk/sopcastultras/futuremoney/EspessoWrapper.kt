package com.vk.sopcastultras.futuremoney

import android.view.View
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions
import com.atiurin.ultron.extensions.click
import io.qameta.allure.kotlin.Allure
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun myStep(name: String, action: () -> Unit) = Allure.step(name) { action.invoke() }

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

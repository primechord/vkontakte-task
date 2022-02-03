package com.vk.sopcastultras.futuremoney.pageelement

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import com.atiurin.ultron.extensions.click
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matcher

object Spinner {
    fun Matcher<View>.selectInSpinner(value: String) {
        this.click()
        Espresso.onData(allOf(`is`(instanceOf(String::class.java)), `is`(value)))
            .perform(ViewActions.click())
    }
}

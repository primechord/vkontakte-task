package com.vk.sopcastultras.futuremoney.pageobjects

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.atdroid.atyurin.futuremoney.R
import com.vk.sopcastultras.futuremoney.BasePage

object AccountsPO : BasePage() {
    private val fab = withId(R.id.fab)

    fun createAccount() = fab.click()

    fun checkSave(expected: String) = withText(expected).checkVisible()

}
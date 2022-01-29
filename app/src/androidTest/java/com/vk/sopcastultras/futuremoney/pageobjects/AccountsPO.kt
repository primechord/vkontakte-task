package com.vk.sopcastultras.futuremoney.pageobjects

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.atdroid.atyurin.futuremoney.R
import com.vk.sopcastultras.futuremoney.BasePage

object AccountsPO : BasePage<AccountsPO>() {
    private val fab = withId(R.id.fab)

    fun createAccount() = fab.click()

    fun checkSave(accountName: String) = withText(accountName).checkVisible()

    fun openAccount(accountName: String) = withText(accountName).click()
}
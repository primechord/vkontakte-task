package com.vk.sopcastultras.futuremoney.pageobjects.income

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.atdroid.atyurin.futuremoney.R
import com.vk.sopcastultras.futuremoney.BasePage
import com.vk.sopcastultras.futuremoney.click
import com.vk.sopcastultras.futuremoney.withIndex

object IncomeListPO : BasePage<IncomeListPO>() {
    private val createButton = withId(R.id.fab)

    fun createIncome() = createButton.click()

    fun openIncome(incomeName: String) = withIndex(withText(incomeName), 0).click()

}

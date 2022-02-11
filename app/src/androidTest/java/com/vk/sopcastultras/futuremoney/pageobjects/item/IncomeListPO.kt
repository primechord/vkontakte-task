package com.vk.sopcastultras.futuremoney.pageobjects.item

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.atdroid.atyurin.futuremoney.R
import com.atiurin.ultron.extensions.click
import com.vk.sopcastultras.futuremoney.BasePage
import com.vk.sopcastultras.futuremoney.myStep
import com.vk.sopcastultras.futuremoney.withIndex

object IncomeListPO : BasePage<IncomeListPO>() {
    private val createButton = withId(R.id.fab)

    fun createIncome() = myStep("Нажать на фаб Создания дохода") { createButton.click() }

    fun openExisting(incomeName: String) {
        myStep("Открыть уже Созданный доход '$incomeName'") {
            withIndex(withText(incomeName), 0).click()
        }
    }

}

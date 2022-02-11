package com.vk.sopcastultras.futuremoney.pageobjects.item

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.atdroid.atyurin.futuremoney.R
import com.atiurin.ultron.extensions.click
import com.vk.sopcastultras.futuremoney.BasePage
import com.vk.sopcastultras.futuremoney.myStep
import com.vk.sopcastultras.futuremoney.withIndex

object OutcomeListPO : BasePage<OutcomeListPO>() {
    private val createButton = withId(R.id.fab)

    fun createOutcome() = myStep("Нажать на фаб Создания расхода") { createButton.click() }

    fun openExisting(outcomeName: String) {
        myStep("Открыть уже Созданный расход '$outcomeName'") {
            withIndex(withText(outcomeName), 0).click()
        }
    }

}

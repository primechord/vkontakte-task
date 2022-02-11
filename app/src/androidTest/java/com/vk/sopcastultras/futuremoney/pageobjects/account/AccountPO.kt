package com.vk.sopcastultras.futuremoney.pageobjects.account

import androidx.test.espresso.matcher.ViewMatchers.withId
import com.atdroid.atyurin.futuremoney.R
import com.atiurin.ultron.extensions.clearText
import com.atiurin.ultron.extensions.click
import com.atiurin.ultron.extensions.typeText
import com.vk.sopcastultras.futuremoney.BasePage
import com.vk.sopcastultras.futuremoney.myStep

object AccountPO : BasePage<AccountPO>() {
    private val nameField = withId(R.id.et_account_name_value)
    private val amountField = withId(R.id.et_account_amount_value)
    private val saveButton = withId(R.id.action_btn_add_budget_item_confirm)

    fun enterName(text: String) {
        myStep("Ввести название '$text'") {
            nameField.clearText()
            nameField.typeText(text)
        }
    }

    fun enterSum(text: String) {
        myStep("Ввести сумму '$text'") {
            amountField.clearText()
            amountField.typeText(text)
        }
    }

    fun saveAccount() = myStep("Нажать на Сохранить") { saveButton.click() }

}

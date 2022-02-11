package com.vk.sopcastultras.futuremoney.pageobjects.account

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.atdroid.atyurin.futuremoney.R
import com.atiurin.ultron.extensions.click
import com.atiurin.ultron.extensions.hasText
import com.vk.sopcastultras.futuremoney.BasePage
import com.vk.sopcastultras.futuremoney.myStep
import com.vk.sopcastultras.futuremoney.withIndex

object AccountListPO : BasePage<AccountListPO>() {
    private val createButton = withId(R.id.fab)
    private val accountName = withId(R.id.tv_budget_item_name)
    private val accountSum = withId(R.id.tv_budget_item_value)

    fun createAccount() = myStep("Нажать на фаб Создания аккаунта") { createButton.click() }

    fun openAccount(accountName: String) {
        myStep("Открыть созданный аккаунт '$accountName'") { withText(accountName).click() }
    }

    fun checkName(expected: String, position: Int = 0) {
        myStep("Ожидаем название '$expected' позиции №$position") {
            withIndex(accountName, position).hasText(expected)
        }
    }

    fun checkSum(expected: String, position: Int = 0) {
        myStep("Ожидаем сумму '$expected' позиции №$position") {
            withIndex(accountSum, position).hasText(expected)
        }
    }
}

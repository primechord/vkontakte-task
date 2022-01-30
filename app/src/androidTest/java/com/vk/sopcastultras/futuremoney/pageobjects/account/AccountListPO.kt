package com.vk.sopcastultras.futuremoney.pageobjects.account

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.atdroid.atyurin.futuremoney.R
import com.atiurin.ultron.extensions.click
import com.vk.sopcastultras.futuremoney.BasePage
import com.vk.sopcastultras.futuremoney.myStep

object AccountListPO : BasePage<AccountListPO>() {
    private val createButton = withId(R.id.fab)

    fun createAccount() = myStep("Нажать на фаб Создания аккаунта") { createButton.click() }

    fun openAccount(accountName: String) {
        myStep("Открыть созданный аккаунт '$accountName'") { withText(accountName).click() }
    }
}

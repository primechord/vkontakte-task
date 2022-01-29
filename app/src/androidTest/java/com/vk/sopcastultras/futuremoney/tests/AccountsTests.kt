package com.vk.sopcastultras.futuremoney.tests

import com.vk.sopcastultras.futuremoney.BaseTest
import com.vk.sopcastultras.futuremoney.pageobjects.AccountPO
import com.vk.sopcastultras.futuremoney.pageobjects.AccountsPO
import com.vk.sopcastultras.futuremoney.pageobjects.Menu
import com.vk.sopcastultras.futuremoney.pageobjects.MenuPO
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test
import java.util.UUID.randomUUID
import kotlin.random.Random

class AccountsTests : BaseTest() {

    override fun setUp() {
        super.setUp()
        MenuPO.goTo(Menu.ACCOUNTS)
    }

    @Test
    @DisplayName("Позитивное создание счета")
    fun fixme() {
        val accountName = randomUUID().toString()
        val accountValue = Random.nextInt(until = 100).toString()

        AccountsPO.createAccount()

        AccountPO.apply {
            enterName(accountName)
            enterAmount(accountValue)
            save()
        }

        AccountsPO.checkSave(accountName)
    }

}
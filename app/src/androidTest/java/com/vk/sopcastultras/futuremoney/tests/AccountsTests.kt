package com.vk.sopcastultras.futuremoney.tests

import com.vk.sopcastultras.futuremoney.*
import com.vk.sopcastultras.futuremoney.pageobjects.account.AccountListPO
import com.vk.sopcastultras.futuremoney.pageobjects.account.AccountPO
import com.vk.sopcastultras.futuremoney.pageobjects.other.Menu
import com.vk.sopcastultras.futuremoney.pageobjects.other.MenuPO
import io.qameta.allure.kotlin.Epic
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Ignore
import org.junit.Test
import kotlin.random.Random

@Epic(SMOKE)
@Feature(ACCOUNTS)
class AccountsTests : BaseTest() {

    @Test
    @DisplayName("Позитивное создание счета")
    fun createAccount() {
        val accountName = "Account ${randomString(15)}"
        val accountValue = Random.nextInt(until = 100).toString()

        MenuPO {
            goTo(Menu.ACCOUNTS)
        }

        AccountListPO {
            createAccount()
        }

        AccountPO {
            enterName(accountName)
            enterAmount(accountValue)
            saveAccount()
        }

        AccountListPO {
            checkText(accountName)
        }
    }

    @Test
    @DisplayName("Редактирование уже созданного")
    fun updateAccount() {
        val accountName = "Account ${randomString(15)}"
        val accountValue = Random.nextDouble(until = 100.0)

        ItemFactory.addAccount {
            name = accountName
            value = accountValue
        }

        MenuPO {
            goTo(Menu.ACCOUNTS)
        }

        AccountListPO {
            openAccount(accountName)
        }

        val updatedAccountName = accountName.reversed()
        val updatedAccountValue = accountValue.plus(50.0).roundDoubleTo2()

        AccountPO {
            enterName(updatedAccountName)
            enterAmount(updatedAccountValue)
            saveAccount()
        }

        AccountListPO {
            checkText(updatedAccountName)
        }
    }

}

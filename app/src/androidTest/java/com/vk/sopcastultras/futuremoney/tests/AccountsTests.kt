package com.vk.sopcastultras.futuremoney.tests

import com.vk.sopcastultras.futuremoney.*
import com.vk.sopcastultras.futuremoney.pageobjects.account.AccountListPO
import com.vk.sopcastultras.futuremoney.pageobjects.account.AccountPO
import com.vk.sopcastultras.futuremoney.pageobjects.other.Menu
import com.vk.sopcastultras.futuremoney.pageobjects.other.MenuPO
import io.qameta.allure.kotlin.Epic
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test
import kotlin.random.Random

@Epic(SMOKE)
@Feature(ACCOUNTS)
class AccountsTests : BaseTest() {

    @Test
    @DisplayName("Создание счета")
    fun createAccount() {
        val expectedAccountName = "Account ${randomString(15)}"
        val expectedAccountSum = Random.nextInt(until = 100).toString()

        MenuPO {
            goTo(Menu.ACCOUNTS)
        }

        AccountListPO {
            createAccount()
        }

        AccountPO {
            enterName(expectedAccountName)
            enterSum(expectedAccountSum)
            saveAccount()
        }

        AccountListPO {
            checkName(expectedAccountName)
            checkSum(expectedAccountSum)
        }
    }

    @Test
    @DisplayName("Редактирование уже созданного счета")
    fun updateAccount() {
        val accountName = "Account ${randomString(15)}"
        val accountSum = Random.nextDouble(until = 100.0)

        ItemFactory.addAccount {
            name = accountName
            value = accountSum
        }

        MenuPO {
            goTo(Menu.ACCOUNTS)
        }

        AccountListPO {
            openAccount(accountName)
        }

        val updatedAccountName = accountName.reversed()
        val updatedAccountSum = accountSum.plus(50.0).roundDoubleTo2()

        AccountPO {
            enterName(updatedAccountName)
            enterSum(updatedAccountSum)
            saveAccount()
        }

        AccountListPO {
            checkName(updatedAccountName)
            checkSum(updatedAccountSum)
        }
    }

}

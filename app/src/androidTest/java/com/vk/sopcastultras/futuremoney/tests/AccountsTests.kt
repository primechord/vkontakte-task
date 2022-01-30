package com.vk.sopcastultras.futuremoney.tests

import com.atdroid.atyurin.futuremoney.serialization.Account
import com.vk.sopcastultras.futuremoney.BaseTest
import com.vk.sopcastultras.futuremoney.LocalDbClient
import com.vk.sopcastultras.futuremoney.pageobjects.account.AccountListPO
import com.vk.sopcastultras.futuremoney.pageobjects.account.AccountPO
import com.vk.sopcastultras.futuremoney.pageobjects.other.Menu
import com.vk.sopcastultras.futuremoney.pageobjects.other.MenuPO
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Ignore
import org.junit.Test
import java.util.UUID.randomUUID
import kotlin.random.Random

class AccountsTests : BaseTest() {

    @Test
    @DisplayName("Позитивное создание счета")
    fun createAccount() {
        MenuPO {
            goTo(Menu.ACCOUNTS)
        }

        val accountName = randomUUID().toString()
        val accountValue = Random.nextInt(until = 100).toString()

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
        // TODO Проверка создания через БД
    }

    @Test
    @DisplayName("Редактирование уже созданного")
    fun updateAccount() {
        val accountName = randomUUID().toString()
        val accountValue = Random.nextDouble(until = 100.0)

        LocalDbClient.addAccount(Account().apply {
            name = accountName
            value = accountValue
        })

        MenuPO {
            goTo(Menu.ACCOUNTS)
        }

        AccountListPO {
            openAccount(accountName)
        }

        val updatedAccountName = accountName.reversed()
        val updatedAccountValue = accountValue.plus(50.0).toString()

        AccountPO {
            enterName(updatedAccountName)
            enterAmount(updatedAccountValue)
            saveAccount()
        }

        AccountListPO {
            checkText(updatedAccountName)
            // checkSaved(updatedAccountValue)
        }
    }

    @Ignore
    @Test
    @DisplayName("Подтверждение без заполнения всего")
    fun emptyFields() {

    }

    @Ignore
    @Test
    @DisplayName("Отображение списочной формы и прокрутка")
    fun listOfAccounts() {

    }

}

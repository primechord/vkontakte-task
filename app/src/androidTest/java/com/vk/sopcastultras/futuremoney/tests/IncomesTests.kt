package com.vk.sopcastultras.futuremoney.tests

import com.atdroid.atyurin.futuremoney.serialization.Income
import com.atdroid.atyurin.futuremoney.utils.DateFormater
import com.vk.sopcastultras.futuremoney.*
import com.vk.sopcastultras.futuremoney.pageobjects.income.BudgetType
import com.vk.sopcastultras.futuremoney.pageobjects.income.IncomeListPO
import com.vk.sopcastultras.futuremoney.pageobjects.income.IncomePO
import com.vk.sopcastultras.futuremoney.pageobjects.other.Menu
import com.vk.sopcastultras.futuremoney.pageobjects.other.MenuPO
import io.qameta.allure.kotlin.Epic
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.joda.time.LocalDate
import org.junit.Ignore
import org.junit.Test
import kotlin.random.Random

@Epic(SMOKE)
@Feature(INCOMES)
class IncomesTests : BaseTest() {

    @Test
    @DisplayName("Позитивное создание прихода")
    fun createIncome() {
        val incomeName = "Income ${generateString(15)}"
        val incomeValue = Random.nextInt(until = 100).toString()
        val incomeDate = LocalDate.now()

        MenuPO {
            goTo(Menu.INCOMES)
        }

        IncomeListPO {
            createIncome()
        }

        IncomePO {
            enterName(incomeName)
            enterAmount(incomeValue)
            selectType(BudgetType.SINGLE)
            selectSingleDate(incomeDate)
            saveIncome()
        }

        IncomeListPO {
            checkText(incomeName)
        }
    }

    @Test
    @DisplayName("Редактирование уже созданного прихода")
    fun updateIncome() {
        val incomeName = "Income ${generateString(15)}"
        val incomeValue = Random.nextDouble(until = 100.0)
        val incomeDate = DateFormater.formatLongToCalendar(System.currentTimeMillis())

        ItemFactory.addIncome(Income().apply {
            name = incomeName
            value = incomeValue
            single_date = incomeDate
            begin_date = incomeDate
            end_date = incomeDate
            type = 0
            period_type = 0
            period_value = 1
        })

        MenuPO {
            goTo(Menu.INCOMES)
        }

        IncomeListPO {
            openIncome(incomeName)
        }

        val updatedIncomeName = incomeName.reversed()
        val updatedIncomeSum = incomeValue.plus(50.0).roundDoubleTo2()
        val updatedIncomeDate = LocalDate.now().minusDays(1)

        IncomePO {
            enterName(updatedIncomeName)
            enterAmount(updatedIncomeSum)
            selectType(BudgetType.SINGLE)
            selectSingleDate(updatedIncomeDate)
            saveIncome()
        }

        IncomeListPO {
            checkText(updatedIncomeName)
        }
    }

}

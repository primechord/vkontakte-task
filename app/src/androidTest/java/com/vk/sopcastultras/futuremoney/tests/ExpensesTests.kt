package com.vk.sopcastultras.futuremoney.tests

import com.atdroid.atyurin.futuremoney.utils.DateFormater
import com.vk.sopcastultras.futuremoney.*
import com.vk.sopcastultras.futuremoney.pageobjects.item.*
import com.vk.sopcastultras.futuremoney.pageobjects.other.Menu
import com.vk.sopcastultras.futuremoney.pageobjects.other.MenuPO
import io.qameta.allure.kotlin.Epic
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.joda.time.LocalDate
import org.junit.Test
import kotlin.random.Random

@Epic(SMOKE)
@Feature(OUTCOMES)
class ExpensesTests : BaseTest() {

    @Test
    @DisplayName("Создание разового расхода")
    fun createExpenseSingle() {
        val expectedItemName = "Outcome ${randomString(15)}"
        val expectedItemSum = Random.nextInt(until = 100).toString()
        val expectedItemDate = LocalDate.now()

        MenuPO {
            goTo(Menu.OUTCOMES)
        }

        OutcomeListPO {
            create()
        }

        ItemPO {
            enterName(expectedItemName)
            enterAmount(expectedItemSum)
            selectBudgetType(BudgetType.SINGLE)
            selectSingleDate(expectedItemDate)
            saveItem()
        }

        OutcomeListPO {
            AssertItem {
                checkName(expectedItemName)
                checkSum(expectedItemSum)
                checkDate(expectedItemDate)
            }
        }
    }

    @Test
    @DisplayName("Создание постоянного расхода")
    fun createExpensePeriodic() {
        val expectedItemName = "Outcome ${randomString(15)}"
        val expectedItemSum = Random.nextInt(until = 100).toString()
        val expectedItemBeginDate = LocalDate.now().minusDays(2)
        val expectedItemEndDate = LocalDate.now().minusDays(1)
        val expectedPeriodValue = "7"

        MenuPO {
            goTo(Menu.OUTCOMES)
        }

        OutcomeListPO {
            create()
        }

        ItemPO {
            enterName(expectedItemName)
            enterAmount(expectedItemSum)

            selectBudgetType(BudgetType.PERIODIC)
            selectDatePeriod(expectedItemBeginDate, expectedItemEndDate)
            enterPeriod(expectedPeriodValue)
            selectPeriodType(PeriodType.MONTH)

            saveItem()
        }

        OutcomeListPO {
            AssertItem {
                checkName(expectedItemName)
                checkSum(expectedItemSum)
                checkDatePeriodic(expectedItemBeginDate, expectedItemEndDate)
            }
        }
    }

    @Test
    @DisplayName("Редактирование уже созданного расхода")
    fun updateExpense() {
        val expectedItemName = "Outcome ${randomString(15)}"
        val expectedItemSum = Random.nextDouble(until = 100.0)
        val expectedItemDate = DateFormater.formatLongToCalendar(System.currentTimeMillis())

        ItemFactory.insertOutcome {
            name = expectedItemName
            value = expectedItemSum
            single_date = expectedItemDate
            begin_date = expectedItemDate
            end_date = expectedItemDate
            type = 0
            period_type = 0
            period_value = 1
        }

        MenuPO {
            goTo(Menu.OUTCOMES)
        }

        OutcomeListPO {
            openExisting(expectedItemName)
        }

        val updatedItemName = expectedItemName.reversed()
        val updatedItemSum = expectedItemSum.plus(50.0).roundDoubleTo2()
        val updatedItemDate = LocalDate.now().minusDays(1)

        ItemPO {
            enterName(updatedItemName)
            enterAmount(updatedItemSum)
            selectBudgetType(BudgetType.SINGLE)
            selectSingleDate(updatedItemDate)
            saveItem()
        }

        OutcomeListPO {
            AssertItem {
                checkName(updatedItemName)
                checkSum(updatedItemSum)
                checkDate(updatedItemDate)
            }
        }
    }

}

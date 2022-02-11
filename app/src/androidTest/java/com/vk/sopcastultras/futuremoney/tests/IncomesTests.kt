package com.vk.sopcastultras.futuremoney.tests

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
@Feature(INCOMES)
class IncomesTests : BaseTest() {

    @Test
    @DisplayName("Создание разового прихода")
    fun createIncomeSingle() {
        val expectedItemName = "Income ${randomString(15)}"
        val expectedItemSum = Random.nextInt(until = 100).toString()
        val expectedItemDate = LocalDate.now()

        MenuPO {
            goTo(Menu.INCOMES)
        }

        IncomeListPO {
            createIncome()
        }

        ItemPO {
            enterName(expectedItemName)
            enterSum(expectedItemSum)
            selectBudgetType(BudgetType.SINGLE)
            selectSingleDate(expectedItemDate)
            saveItem()
        }

        IncomeListPO {
            AssertItem {
                checkName(expectedItemName)
                checkSum(expectedItemSum)
                checkDate(expectedItemDate)
            }
        }
    }

    @Test
    @DisplayName("Создание постоянного прихода")
    fun createIncomePeriodic() {
        val expectedItemName = "Income ${randomString(15)}"
        val expectedItemSum = Random.nextInt(until = 100).toString()
        val expectedItemBeginDate = LocalDate.now().minusDays(2)
        val expectedItemEndDate = LocalDate.now().minusDays(1)
        val expectedItemPeriod = 7

        MenuPO {
            goTo(Menu.INCOMES)
        }

        IncomeListPO {
            createIncome()
        }

        ItemPO {
            enterName(expectedItemName)
            enterSum(expectedItemSum)

            selectBudgetType(BudgetType.PERIODIC)
            selectDatePeriod(expectedItemBeginDate, expectedItemEndDate)
            enterPeriod(expectedItemPeriod)
            selectPeriodType(PeriodType.MONTH)

            saveItem()
        }

        IncomeListPO {
            AssertItem {
                checkName(expectedItemName)
                checkSum(expectedItemSum)
                checkDatePeriodic(expectedItemBeginDate, expectedItemEndDate)
            }
        }
    }

    @Test
    @DisplayName("Редактирование уже созданного прихода")
    fun updateIncome() {
        val itemName = "Income ${randomString(15)}"
        val itemSum = Random.nextDouble(until = 100.0)
        val itemDate = ItemFactory.currentDate()

        ItemFactory.insertIncome {
            name = itemName
            value = itemSum
            single_date = itemDate
            begin_date = itemDate
            end_date = itemDate
            type = 0
            period_type = 0
            period_value = 1
        }

        MenuPO {
            goTo(Menu.INCOMES)
        }

        IncomeListPO {
            openExisting(itemName)
        }

        val updatedItemName = itemName.reversed()
        val updatedItemSum = itemSum.plus(50.0).roundDoubleTo2()
        val updatedItemDate = LocalDate.now().minusDays(1)

        ItemPO {
            enterName(updatedItemName)
            enterSum(updatedItemSum)
            selectBudgetType(BudgetType.SINGLE)
            selectSingleDate(updatedItemDate)
            saveItem()
        }

        IncomeListPO {
            AssertItem {
                checkName(updatedItemName)
                checkSum(updatedItemSum)
                checkDate(updatedItemDate)
            }
        }
    }

}

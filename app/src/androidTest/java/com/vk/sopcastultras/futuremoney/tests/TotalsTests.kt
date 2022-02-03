package com.vk.sopcastultras.futuremoney.tests

import com.vk.sopcastultras.futuremoney.*
import com.vk.sopcastultras.futuremoney.pageobjects.totals.TotalBeginTypes
import com.vk.sopcastultras.futuremoney.pageobjects.totals.TotalsPO
import io.qameta.allure.kotlin.Epic
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.joda.time.LocalDate
import org.junit.Test

@Epic(SMOKE)
@Feature(TOTALS)
class TotalsTests : BaseTest() {

    @Test
    @DisplayName("Отобразить итоги для двух границ")
    fun withBegin() {
        val expectedAccountsTotal = "50"
        val expectedIncomeTotal = "150"
        val expectedOutcomeTotal = "100"
        val expectedGeneralTotal = "100"
        val currentDate = ItemFactory.currentDate()

        repeat(5) {
            ItemFactory.addAccount {
                name = randomString()
                value = 10.0
            }

            ItemFactory.insertOutcome {
                name = randomString()
                value = 20.0
                single_date = currentDate
                begin_date = currentDate
                end_date = currentDate
                type = 0
                period_type = 0
                period_value = 1
            }

            ItemFactory.insertIncome {
                name = randomString()
                value = 30.0
                single_date = currentDate
                begin_date = currentDate
                end_date = currentDate
                type = 0
                period_type = 0
                period_value = 1
            }
        }

        TotalsPO {
            selectPeriod(TotalBeginTypes.SPECIFIC_DAY)
            selectDate(LocalDate.now().minusDays(1), datePickerNumber = 0)
            selectDate(LocalDate.now().plusDays(1), datePickerNumber = 1)
            calculate()

            checkAccountsTotal(expectedAccountsTotal)
            checkOutcomesTotal(expectedOutcomeTotal)
            checkIncomesTotal(expectedIncomeTotal)
            checkGeneralTotal(expectedGeneralTotal)
        }
    }

    @Test
    @DisplayName("Отобразить итоги за все время")
    fun withoutBegin() {
        val expectedAccountsTotal = "50"
        val expectedIncomeTotal = "150"
        val expectedOutcomeTotal = "100"
        val expectedGeneralTotal = "100"
        val currentDate = ItemFactory.currentDate()

        repeat(5) {
            ItemFactory.addAccount {
                name = randomString()
                value = 10.0
            }

            ItemFactory.insertOutcome {
                name = randomString()
                value = 20.0
                single_date = currentDate
                begin_date = currentDate
                end_date = currentDate
                type = 0
                period_type = 0
                period_value = 1
            }

            ItemFactory.insertIncome {
                name = randomString()
                value = 30.0
                single_date = currentDate
                begin_date = currentDate
                end_date = currentDate
                type = 0
                period_type = 0
                period_value = 1
            }
        }

        TotalsPO {
            selectPeriod(TotalBeginTypes.ALL_TIME)
            selectDate(LocalDate.now().plusDays(1), datePickerNumber = 1)
            calculate()

            checkAccountsTotal(expectedAccountsTotal)
            checkOutcomesTotal(expectedOutcomeTotal)
            checkIncomesTotal(expectedIncomeTotal)
            checkGeneralTotal(expectedGeneralTotal)
        }
    }

}

package com.vk.sopcastultras.futuremoney.pageobjects.income

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.atdroid.atyurin.futuremoney.R
import com.atiurin.ultron.extensions.clearText
import com.atiurin.ultron.extensions.click
import com.atiurin.ultron.extensions.typeText
import com.vk.sopcastultras.futuremoney.BasePage
import com.vk.sopcastultras.futuremoney.myStep
import com.vk.sopcastultras.futuremoney.pageelement.Calendar
import com.vk.sopcastultras.futuremoney.selectInSpinner
import org.hamcrest.CoreMatchers.allOf
import org.joda.time.LocalDate

enum class BudgetType {
    SINGLE, PERIODIC
}

enum class PeriodType {
    DAY, WEEK, MONTH, YEAR
}

object IncomePO : BasePage<IncomePO>() {
    private val nameField = withId(R.id.et_item_name_value)
    private val amountField = withId(R.id.et_item_amount_value)
    private val incomeTypeSpinner = withId(R.id.sp_item_type)
    private val saveButton = withId(R.id.action_btn_add_budget_item_confirm)

    private val singleDateElement = allOf(withId(R.id.ll_item_single_date), isDisplayed())
    private val beginDateElement = withId(R.id.ll_item_begin_date)
    private val endDateElement = withId(R.id.ll_item_end_date)
    private val periodValue = withId(R.id.et_period_value)
    private val periodTypeSpinner = withId(R.id.sp_period_type_spinner)

    fun enterName(text: String) {
        myStep("Ввести имя '$text'") {
            nameField.clearText()
            nameField.typeText(text)
        }
    }

    fun enterAmount(text: String) {
        myStep("Ввести сумму '$text'") {
            amountField.clearText()
            amountField.typeText(text)
        }
    }

    // Можно было поддержать не только RU
    fun selectBudgetType(budgetType: BudgetType) {
        myStep("Выбрать тип Дохода '$budgetType'") {
            when (budgetType) {
                BudgetType.PERIODIC -> incomeTypeSpinner.selectInSpinner("Постоянный")
                BudgetType.SINGLE -> incomeTypeSpinner.selectInSpinner("Разовый")
            }
        }
    }

    fun selectSingleDate(singleInput: LocalDate) {
        myStep("Выбираем дату $singleInput") {
            Calendar.selectDate(singleDateElement, singleInput)
        }
    }

    fun selectDatePeriod(beginDateInput: LocalDate, endDateInput: LocalDate) {
        myStep("Выбираем период дат с $beginDateInput по $endDateInput") {
            Calendar.selectDate(beginDateElement, beginDateInput)
            Calendar.selectDate(endDateElement, endDateInput)
        }
    }

    fun enterPeriod(text: String) {
        myStep("Ввести период '$text'") {
            periodValue.clearText()
            periodValue.typeText(text)
        }
    }

    fun selectPeriodType(periodType: PeriodType) {
        myStep("Выбрать тип периода '$periodType'") {
            when (periodType) {
                PeriodType.DAY -> periodTypeSpinner.selectInSpinner("День")
                PeriodType.WEEK -> periodTypeSpinner.selectInSpinner("Неделя")
                PeriodType.MONTH -> periodTypeSpinner.selectInSpinner("Месяц")
                PeriodType.YEAR -> periodTypeSpinner.selectInSpinner("Год")
            }
        }
    }

    fun saveIncome() = myStep("Нажать на Сохранить") { saveButton.click() }

}

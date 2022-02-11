package com.vk.sopcastultras.futuremoney.pageobjects.item

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.atdroid.atyurin.futuremoney.R
import com.atiurin.ultron.extensions.clearText
import com.atiurin.ultron.extensions.click
import com.atiurin.ultron.extensions.typeText
import com.vk.sopcastultras.futuremoney.BasePage
import com.vk.sopcastultras.futuremoney.myStep
import com.vk.sopcastultras.futuremoney.pageelement.Calendar
import com.vk.sopcastultras.futuremoney.pageelement.Spinner.selectInSpinner
import org.hamcrest.CoreMatchers.allOf
import org.joda.time.LocalDate

enum class BudgetType {
    SINGLE, PERIODIC
}

enum class PeriodType {
    DAY, WEEK, MONTH, YEAR
}

object ItemPO : BasePage<ItemPO>() {
    private val nameField = withId(R.id.et_item_name_value)
    private val amountField = withId(R.id.et_item_amount_value)
    private val itemTypeSpinner = withId(R.id.sp_item_type)
    private val saveButton = withId(R.id.action_btn_add_budget_item_confirm)

    private val singleDateElement = allOf(withId(R.id.ll_item_single_date), isDisplayed())
    private val beginDateElement = withId(R.id.ll_item_begin_date)
    private val endDateElement = withId(R.id.ll_item_end_date)
    private val periodField = withId(R.id.et_period_value)
    private val periodTypeSpinner = withId(R.id.sp_period_type_spinner)

    fun enterName(text: String) {
        myStep("Ввести название '$text'") {
            nameField.clearText()
            nameField.typeText(text)
        }
    }

    fun enterSum(text: String) {
        myStep("Ввести сумму '$text'") {
            amountField.clearText()
            amountField.typeText(text)
        }
    }

    // Можно было поддержать не только RU
    fun selectBudgetType(budgetType: BudgetType) {
        myStep("Выбрать тип движения '$budgetType'") {
            when (budgetType) {
                BudgetType.PERIODIC -> itemTypeSpinner.selectInSpinner("Постоянный")
                BudgetType.SINGLE -> itemTypeSpinner.selectInSpinner("Разовый")
            }
        }
    }

    fun selectSingleDate(singleInput: LocalDate) {
        myStep("Выбираем дату $singleInput") {
            Calendar.selectDate(singleDateElement, singleInput)
        }
    }

    fun selectDatePeriod(beginDateInput: LocalDate, endDateInput: LocalDate) {
        myStep("Выбираем период дат с '$beginDateInput' по '$endDateInput'") {
            Calendar.selectDate(beginDateElement, beginDateInput)
            Calendar.selectDate(endDateElement, endDateInput)
        }
    }

    fun enterPeriod(text: Int) {
        myStep("Ввести период '$text'") {
            periodField.clearText()
            periodField.typeText(text.toString())
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

    fun saveItem() = myStep("Нажать на Сохранить") { saveButton.click() }

}

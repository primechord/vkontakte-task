package com.vk.sopcastultras.futuremoney.pageobjects.income

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.atdroid.atyurin.futuremoney.R
import com.vk.sopcastultras.futuremoney.BasePage
import com.vk.sopcastultras.futuremoney.click
import com.vk.sopcastultras.futuremoney.pageelement.Calendar
import com.vk.sopcastultras.futuremoney.selectInSpinner
import com.vk.sopcastultras.futuremoney.typeText
import org.hamcrest.CoreMatchers.allOf
import org.joda.time.LocalDate

enum class BudgetType {
    SINGLE, PERIODIC
}

object IncomePO : BasePage<IncomePO>() {
    private val nameField = withId(R.id.et_item_name_value)
    private val amountField = withId(R.id.et_item_amount_value)
    private val typeSpinner = withId(R.id.sp_item_type)
    private val saveButton = withId(R.id.action_btn_add_budget_item_confirm)

    private val singleDate = allOf(withId(R.id.ll_item_single_date), isDisplayed())
    private val startDate = withId(R.id.ll_item_begin_date)
    private val endDate = withId(R.id.ll_item_end_date)

    private val okInCalendar = withId(android.R.id.button1)

    fun enterName(text: String) = nameField.typeText(text)

    fun enterAmount(text: String) = amountField.typeText(text)

    // Можно было поддержать не только RU
    fun selectType(budgetType: BudgetType) = when (budgetType) {
        BudgetType.PERIODIC -> typeSpinner.selectInSpinner("Постоянный")
        BudgetType.SINGLE -> typeSpinner.selectInSpinner("Разовый")
    }

    // Можно было поддержать выбор периода дат
    fun selectSingleDate(localDate: LocalDate) {
        singleDate.click()
        Calendar.selectDate(localDate.year, localDate.monthOfYear, localDate.dayOfMonth)
        okInCalendar.click()
    }

    fun saveIncome() = saveButton.click()

}

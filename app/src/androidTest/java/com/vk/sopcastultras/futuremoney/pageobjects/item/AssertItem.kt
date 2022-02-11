package com.vk.sopcastultras.futuremoney.pageobjects.item

import androidx.test.espresso.matcher.ViewMatchers
import com.atdroid.atyurin.futuremoney.R
import com.atiurin.ultron.extensions.hasText
import com.vk.sopcastultras.futuremoney.BasePage
import com.vk.sopcastultras.futuremoney.myStep
import com.vk.sopcastultras.futuremoney.withIndex
import org.joda.time.LocalDate

object AssertItem : BasePage<AssertItem>() {
    private val itemName = ViewMatchers.withId(R.id.tv_budget_item_name)
    private val itemSum = ViewMatchers.withId(R.id.tv_budget_item_value)
    private val itemDate = ViewMatchers.withId(R.id.tv_budget_item_date)

    fun checkName(expected: String, position: Int = 0) {
        myStep("Проверяем название '$expected' позиции №$position") {
            withIndex(itemName, position).hasText(expected)
        }
    }

    fun checkSum(expected: String, position: Int = 0) {
        myStep("Проверяем сумму '$expected' позиции №$position") {
            withIndex(itemSum, position).hasText(expected)
        }
    }

    fun checkDate(expected: LocalDate, position: Int = 0) {
        myStep("Проверяем дату '$expected' позиции №$position") {
            withIndex(itemDate, position).hasText(expected.toString("dd.MM.yy"))
        }
    }

    fun checkDatePeriodic(start: LocalDate, end: LocalDate, position: Int = 0) {
        myStep("Проверяем период с '$start' по '$end' позиции №$position") {
            val expected = "с ${start.toString("dd.MM.yy")} до ${end.toString("dd.MM.yy")}"
            withIndex(itemDate, position).hasText(expected)
        }
    }

}

package com.vk.sopcastultras.futuremoney.pageobjects

import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.atdroid.atyurin.futuremoney.R
import com.vk.sopcastultras.futuremoney.BasePage

enum class Menu {
    TOTALS, ACCOUNTS, INCOMES, EXPENSES, ABOUT_APP
}

object MenuPO : BasePage() {
    private val totals = withId(R.id.nav_totals)
    private val accounts = withId(R.id.nav_accounts)
    private val incomes = withId(R.id.nav_incomes)
    private val outcomes = withId(R.id.nav_outcomes)
    private val about = withId(R.id.nav_about)
    private val toolbar = withContentDescription(R.string.navigation_drawer_open)

    fun goTo(menu: Menu) {
        toolbar.click()
        when (menu) {
            Menu.TOTALS -> totals.click()
            Menu.ACCOUNTS -> accounts.click()
            Menu.INCOMES -> incomes.click()
            Menu.EXPENSES -> outcomes.click()
            Menu.ABOUT_APP -> about.click()
        }
    }

}
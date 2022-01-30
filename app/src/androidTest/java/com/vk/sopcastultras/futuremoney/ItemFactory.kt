package com.vk.sopcastultras.futuremoney

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.atdroid.atyurin.futuremoney.dao.AccountsDAO
import com.atdroid.atyurin.futuremoney.dao.IncomesDAO
import com.atdroid.atyurin.futuremoney.serialization.Account
import com.atdroid.atyurin.futuremoney.serialization.Income

object ItemFactory {

    fun addAccount(account: Account) {
        AccountsDAO(getInstrumentation().targetContext).run {
            openWritable()
            addAccount(account)
            close()
        }

    }

    fun addIncome(income: Income) {
        IncomesDAO(getInstrumentation().targetContext).run {
            openWritable()
            addIncome(income)
            close()
        }

    }
}

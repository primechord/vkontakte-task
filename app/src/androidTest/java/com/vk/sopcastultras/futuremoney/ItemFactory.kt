package com.vk.sopcastultras.futuremoney

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.atdroid.atyurin.futuremoney.dao.AccountsDAO
import com.atdroid.atyurin.futuremoney.dao.IncomesDAO
import com.atdroid.atyurin.futuremoney.dao.OutcomesDAO
import com.atdroid.atyurin.futuremoney.serialization.Account
import com.atdroid.atyurin.futuremoney.serialization.Income
import com.atdroid.atyurin.futuremoney.serialization.Outcome

object ItemFactory {

    fun addAccount(param: Account.() -> Unit) {
        AccountsDAO(getInstrumentation().targetContext).run {
            openWritable()
            val account = Account().apply(param)
            myStep("Insert Account ${account.name}") { addAccount(account) }
            close()
        }
    }

    fun insertIncome(param: Income.() -> Unit) {
        IncomesDAO(getInstrumentation().targetContext).run {
            openWritable()
            val income = Income().apply(param)
            myStep("INSERT Income ${income.name}") { addIncome(income) }
            close()
        }
    }

    fun insertOutcome(param: Outcome.() -> Unit) {
        OutcomesDAO(getInstrumentation().targetContext).run {
            openWritable()
            val outcome = Outcome().apply(param)
            myStep("INSERT Outcome ${outcome.name}") { addOutcome(outcome) }
            close()
        }
    }

}

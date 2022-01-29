package com.vk.sopcastultras.futuremoney

import androidx.test.platform.app.InstrumentationRegistry
import com.atdroid.atyurin.futuremoney.dao.AccountsDAO
import com.atdroid.atyurin.futuremoney.serialization.Account

object LocalDbClient {

    fun addAccount(account: Account) {
        val dao = AccountsDAO(InstrumentationRegistry.getInstrumentation().targetContext)
        dao.openWritable()
        dao.addAccount(account)
        dao.close()
    }
}

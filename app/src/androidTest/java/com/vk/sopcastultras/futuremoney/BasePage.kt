package com.vk.sopcastultras.futuremoney

import androidx.test.espresso.matcher.ViewMatchers.withText
import com.atiurin.ultron.extensions.isDisplayed

abstract class BasePage<T> {

    // magic from internet plus my idea
    operator fun <R> invoke(block: T.() -> R) {
        myStep("📱 ${this.javaClass.simpleName}") {
            block.invoke(this as T)
        }
    }

    fun checkText(accountName: String) {
        myStep("Проверяем видимость текста '$accountName'") {
            withIndex(withText(accountName), 0).isDisplayed() // FIXME временное решение
        }
    }
}

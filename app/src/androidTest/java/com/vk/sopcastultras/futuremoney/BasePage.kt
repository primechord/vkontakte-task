package com.vk.sopcastultras.futuremoney

abstract class BasePage<T> {

    // magic from internet plus my idea
    operator fun <R> invoke(block: T.() -> R) {
        myStep("📱 ${this.javaClass.simpleName}") {
            block.invoke(this as T)
        }
    }

}

package com.vk.sopcastultras.futuremoney

import java.util.*

fun randomString(length: Int = 10): String {
    require(length <= 36)
    return UUID.randomUUID().toString().take(length)
}

fun Double.roundDoubleTo2() = "%.2f".format(this)

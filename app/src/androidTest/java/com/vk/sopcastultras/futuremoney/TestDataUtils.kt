package com.vk.sopcastultras.futuremoney

import java.util.*

fun generateString(length: Int): String {
    require(length <= 36)
    return UUID.randomUUID().toString().take(length)
}
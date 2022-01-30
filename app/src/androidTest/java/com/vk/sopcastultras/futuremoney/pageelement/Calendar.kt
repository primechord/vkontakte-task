package com.vk.sopcastultras.futuremoney.pageelement

import android.widget.DatePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import org.hamcrest.Matchers.equalTo

object Calendar {
    fun selectDate(year: Int, month: Int, day: Int) {
        onView(withClassName(equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(year, month, day))
    }
}

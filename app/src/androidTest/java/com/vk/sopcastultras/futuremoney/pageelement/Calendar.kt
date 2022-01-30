package com.vk.sopcastultras.futuremoney.pageelement

import android.widget.DatePicker
import androidx.test.espresso.Espresso
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers
import com.vk.sopcastultras.futuremoney.myStep
import org.hamcrest.Matchers

object Calendar {
    fun selectDate(year: Int, month: Int, day: Int) {
        myStep("Выбираем дату $day $month $year") {
            Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name)))
                .perform(PickerActions.setDate(year, month, day))
        }
    }
}

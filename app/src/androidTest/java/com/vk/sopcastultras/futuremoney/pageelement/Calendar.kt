package com.vk.sopcastultras.futuremoney.pageelement

import android.R
import android.view.View
import android.widget.DatePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.atiurin.ultron.extensions.click
import org.hamcrest.Matcher
import org.hamcrest.Matchers.equalTo
import org.joda.time.LocalDate

object Calendar {
    private val okInCalendar = withId(R.id.button1)

    fun selectDate(calendarMatcher: Matcher<View>, date: LocalDate) {
        calendarMatcher.click()
        onView(withClassName(equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(date.year, date.monthOfYear, date.dayOfMonth))
        okInCalendar.click()
    }
}

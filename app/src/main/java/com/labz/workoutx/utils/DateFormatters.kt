package com.labz.workoutx.utils

import com.labz.workoutx.utils.Consts.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateFormatters {
    // Date formatters
    private fun stringToCalendarObj(date: String): Calendar = Calendar.getInstance().apply {
        time = date.let {
            // Parse the date string to a Date object
            SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(it) ?: Date()
        }
    }

    private fun calendarObjectToString(calendar: Calendar) =
        SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(calendar.time)

    fun Calendar.toFormattedString() = calendarObjectToString(this)

    fun String.toFormattedDateString() = calendarObjectToString(stringToCalendarObj(this))

    fun String.toCalendarObj() = stringToCalendarObj(this)
}
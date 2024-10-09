package com.labz.workoutx.exts

import android.os.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.LocalDate
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= System.currentTimeMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            year <= LocalDate.now().year
        } else {
            year <= Calendar.getInstance().get(Calendar.YEAR)
        }
    }
}
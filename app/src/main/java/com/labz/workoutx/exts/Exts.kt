package com.labz.workoutx.exts

import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale

object Exts {

    fun LocalDateTime.getRange(): String {
        val currentMonth = this.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val nextMonth = this.plusDays(6).month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        return "${this.dayOfMonth}, $currentMonth - ${this.plusDays(6).dayOfMonth}, $nextMonth"
    }

    fun LocalDateTime.getKey(): String {
        return "${this.year}-${this.monthValue}-${this.dayOfMonth}"
    }
}
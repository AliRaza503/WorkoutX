package com.labz.workoutx.uistates

import com.labz.workoutx.exts.ProgressTab
import com.labz.workoutx.models.ProgressData
import java.time.LocalDateTime

data class ProgressesUiState(
    val dateRange: String = "",
    val selectedTabIndex: ProgressTab = ProgressTab.STEPS,
    val isCircularProgressIndicatorVisible: Boolean = false,
    val oneWeekData: Array<ProgressData?> = arrayOfNulls(7),
    val sunday: LocalDateTime? = null,
    val saturday: LocalDateTime? = null,
    val dataIsOfCurrentWeek : Boolean = false,
    val dataIsOfVeryLastWeek: Boolean = false
)


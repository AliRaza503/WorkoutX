package com.labz.workoutx.uistates

import com.labz.workoutx.models.Workout

data class PerformWorkoutUiState(
    val isCircularProgressIndicatorVisible: Boolean = false,
    val workout: Workout? = null,
    val countdownValue: Int? = null, // To track 3, 2, 1, Go
    val remainingTime: Long = 0L, // To track countdown timer
    val isTimerRunning: Boolean = true, // To track if the timer is running
    val isWorkoutLoaded: Boolean = false,
    val getRepsDialogBoxVisible: Boolean = false,
)

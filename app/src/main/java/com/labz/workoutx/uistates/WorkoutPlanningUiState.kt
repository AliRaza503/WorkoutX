package com.labz.workoutx.uistates

import com.labz.workoutx.models.Workout

data class WorkoutPlanningUiState (
    val isCircularProgressIndicatorVisible: Boolean = false,
    val workout: Workout? = null
)
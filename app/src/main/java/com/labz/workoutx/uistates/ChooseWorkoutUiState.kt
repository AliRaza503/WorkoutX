package com.labz.workoutx.uistates

import com.labz.workoutx.models.Goal
import com.labz.workoutx.models.Workout

data class ChooseWorkoutUiState(
    val isCircularProgressIndicatorVisible: Boolean = false,
    val allWorkouts: Map<Goal, List<Workout>> = emptyMap(),
    val filteredWorkouts: List<Workout> = emptyList(),
    val selectedGoalsList: List<Goal> = emptyList()
)
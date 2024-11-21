package com.labz.workoutx.uistates

import com.labz.workoutx.models.Workout

data class DashboardUiState(
    val userName: String = "",
    val profilePictureUrl: String = "",
    val isCircularProgressIndicatorVisible: Boolean = false,
    val allPermissionsGranted: Boolean = false,
    val areProgressesLoaded: Boolean = false,
    val bottomBarSelectedItemIndex: Int = 0,
    val workoutHistory: List<Pair<String, Workout>> = emptyList(),

    val stepsOfToday: Int = 0,
    val activeCaloriesBurnedToday: Double = 0.0,
    val totalCaloriesBurnedToday: Double = 0.0,
    val heartRateNow: Long = 0,
    val weightInKgs: Double = 0.0,
    val heightInCms: Double = 0.0,
    val exerciseSessionOfToday: String = "",
    )
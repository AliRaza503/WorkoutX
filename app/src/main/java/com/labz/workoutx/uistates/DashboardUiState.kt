package com.labz.workoutx.uistates

data class DashboardUiState(
    val userName: String = "",
    val profilePictureUrl: String = "",
    val isCircularProgressIndicatorVisible: Boolean = false,
    val allPermissionsGranted: Boolean = false,

    val stepsOfToday: Int = 0,
    val activeCaloriesBurnedToday: Double = 0.0,
    val totalCaloriesBurnedToday: Double = 0.0,
    val heartRateNow: Long = 0,
    val weightInKgs: Double = 0.0,
    val heightInCms: Double = 0.0,
    val sleepSessionOfToday: String = "",
    val exerciseSessionOfToday: String = "",
)
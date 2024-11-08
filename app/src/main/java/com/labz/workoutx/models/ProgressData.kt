package com.labz.workoutx.models


data class ProgressData(
    val stepsData: Long,
    val caloriesBurned: Double,
    val minutesActive: Double
) {
    companion object TargetProgressData {
        const val TARGET_STEPS = 10000f
        const val TARGET_TOTAL_CALORIES_BURNED = 2000f
        const val TARGET_MINUTES_ACTIVE_PER_DAY = 30f
    }
}
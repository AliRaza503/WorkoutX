package com.labz.workoutx.models

import androidx.health.connect.client.records.SleepSessionRecord.Stage

data class ProgressData(
    val stepsData: Long,
    val caloriesBurned: Double,
    val weight: Double,
    val sleepScore: List<Stage>
) {
    companion object TargetProgressData {
        val TARGET_STEPS = 10000f
        val TARGET_TOTAL_CALORIES_BURNED = 2000f
        val TARGET_WEIGHT = 70.0f
        val TARGET_DEEP_SLEEP_MINUTES = 360
    }
}
package com.labz.workoutx.services.db

import com.labz.workoutx.models.Goal


interface DBService {
    suspend fun setUserData(
        weightInKgs: Double,
        heightInCms: Double,
        dateOfBirth: String,    // Formatted As Consts.DATE_FORMAT
        gender: String,
        minutesOfExercisePerDay: Double
    )

    suspend fun getUserDataToUserObj()
    suspend fun checkIfUserInfoLoaded(): Boolean
    suspend fun getActivityMinutesForMonth(): Map<String, Double>
    suspend fun addActivityMinutesOfToday(minutes: Double)
    suspend fun setGoal(goal: Goal)
}
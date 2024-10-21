package com.labz.workoutx.services.db

interface DBService {
    suspend fun setUserData(
        weightInKgs: Double,
        heightInCms: Double,
        dateOfBirth: String,    // Formatted As Consts.DATE_FORMAT
        gender: String,
        goal: String
    )

    suspend fun getUserDataToUserObj()
    suspend fun checkIfUserInfoLoaded(): Boolean
}
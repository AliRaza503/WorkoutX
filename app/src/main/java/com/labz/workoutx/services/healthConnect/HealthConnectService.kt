package com.labz.workoutx.services.healthConnect

import androidx.health.connect.client.HealthConnectClient

interface HealthConnectService {
    suspend fun readTodaySteps(healthConnectClient: HealthConnectClient): Int
    suspend fun readHeartRateNow(healthConnectClient: HealthConnectClient): Long
    suspend fun readActiveCaloriesBurnedToday(healthConnectClient: HealthConnectClient): Double
    suspend fun readTotalCaloriesBurnedToday(healthConnectClient: HealthConnectClient): Double
    suspend fun readWeight(healthConnectClient: HealthConnectClient): Double
    suspend fun readHeight(healthConnectClient: HealthConnectClient): Double
    suspend fun readSleepSessionOfToday(healthConnectClient: HealthConnectClient): String
    suspend fun readExerciseSessionOfToday(healthConnectClient: HealthConnectClient): String

}
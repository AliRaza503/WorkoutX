package com.labz.workoutx.services.healthConnect

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.SleepSessionRecord.Stage

interface HealthConnectService {
    suspend fun readStepsForLast30Days(healthConnectClient: HealthConnectClient): Map<String, Long>
    suspend fun readTotalCaloriesBurnedForLast30Days(healthConnectClient: HealthConnectClient): Map<String, Double>
    suspend fun readActiveCaloriesBurnedForLast30Days(healthConnectClient: HealthConnectClient): Map<String, Double>
    suspend fun readSleepSessionsForLast30Days(healthConnectClient: HealthConnectClient): Map<String, List<Stage>>

    suspend fun readWeight(healthConnectClient: HealthConnectClient): Map<String, Double>
}
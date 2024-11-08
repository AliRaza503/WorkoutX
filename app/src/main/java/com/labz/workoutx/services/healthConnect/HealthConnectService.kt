package com.labz.workoutx.services.healthConnect

import androidx.health.connect.client.HealthConnectClient

interface HealthConnectService {
    suspend fun readStepsForLast30Days(healthConnectClient: HealthConnectClient): Map<String, Long>
    suspend fun readTotalCaloriesBurnedForLast30Days(healthConnectClient: HealthConnectClient): Map<String, Double>
}
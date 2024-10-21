package com.labz.workoutx.services.permissions

import androidx.health.connect.client.HealthConnectClient

interface PermissionsService {
    suspend fun hasAllPermissions(healthConnectClient: HealthConnectClient): Boolean
    fun getPermissionsSet(): Set<String>
}
package com.labz.workoutx.services.permissions

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.PermissionController.Companion
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord

class PermissionsServiceImpl : PermissionsService {
    private val changesDataTypes = setOf(
        HeartRateRecord::class,
        StepsRecord::class,
        ActiveCaloriesBurnedRecord::class,
        TotalCaloriesBurnedRecord::class,
        ExerciseSessionRecord::class,
        StepsRecord::class,
        WeightRecord::class,
        HeightRecord::class,
        SleepSessionRecord::class
    )

    private val permissions = changesDataTypes.flatMap { dataType ->
        listOf(
            HealthPermission.getReadPermission(dataType),
            HealthPermission.getWritePermission(dataType)
        )
    }.toSet()

    /**
     * Determines whether all the specified permissions are already granted. It is recommended to
     * call [PermissionController.getGrantedPermissions] first in the permissions flow, as if the
     * permissions are already granted then there is no need to request permissions via
     * [PermissionController.createRequestPermissionResultContract].
     */
    override suspend fun hasAllPermissions(healthConnectClient: HealthConnectClient): Boolean {
        return healthConnectClient.permissionController.getGrantedPermissions()
            .containsAll(permissions)
    }

    override fun getPermissionsSet(): Set<String> = permissions

}
package com.labz.workoutx.services.healthConnect

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.labz.workoutx.utils.Consts
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class HealthConnectServiceImpl : HealthConnectService {
    override suspend fun readTodaySteps(healthConnectClient: HealthConnectClient): Int {
        try {
            val yesterday = LocalDateTime.now().minusDays(1)
            val startTime = yesterday.withHour(0).withMinute(0).withSecond(0).atZone(ZoneId.systemDefault()).toInstant()
            val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // The result may be null if no data is available in the time range
            val stepCount = response[StepsRecord.COUNT_TOTAL]
            Log.d("${Consts.LOG_TAG}_HealthConnectService", "readTodaySteps: $stepCount")
            return stepCount?.toInt() ?: 0
        } catch (e: Exception) {
            // Run error handling here.
            Log.e("${Consts.LOG_TAG}_HealthConnectService", "readTodaySteps: ${e.localizedMessage}")
        }
        return 0
    }

    override suspend fun readHeartRateNow(healthConnectClient: HealthConnectClient): Long {
        try {
            val startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
                .atZone(ZoneId.systemDefault()).toInstant().also { it ->
                    it.minus(1, ChronoUnit.MONTHS)
                }
            val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    HeartRateRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // Get the most recent heart rate record
            val heartRate = response.records.lastOrNull()?.samples?.lastOrNull()?.beatsPerMinute
            Log.d("${Consts.LOG_TAG}_HealthConnectService", "readHeartRateNow: $heartRate")
            return heartRate ?: 0L
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readHeartRateNow: ${e.localizedMessage}"
            )
        }
        return 0L
    }

    // Function to read active calories burned today
    override suspend fun readActiveCaloriesBurnedToday(healthConnectClient: HealthConnectClient): Double {
        try {
            val startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
                .atZone(ZoneId.systemDefault()).toInstant()
            val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            val activeCalories = response[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]
            Log.d(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readActiveCaloriesBurnedToday: $activeCalories"
            )
            return activeCalories?.inCalories ?: 0.0
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readActiveCaloriesBurnedToday: ${e.localizedMessage}"
            )
        }
        return 0.0
    }

    override suspend fun readTotalCaloriesBurnedToday(healthConnectClient: HealthConnectClient): Double {
        try {
            val startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
                .atZone(ZoneId.systemDefault()).toInstant()
            val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(TotalCaloriesBurnedRecord.ENERGY_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            val totalCalories = response[TotalCaloriesBurnedRecord.ENERGY_TOTAL]
            Log.d(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readTotalCaloriesBurnedToday: $totalCalories"
            )
            return totalCalories?.inCalories ?: 0.0
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readTotalCaloriesBurnedToday: ${e.localizedMessage}"
            )
        }
        return 0.0
    }

    override suspend fun readWeight(healthConnectClient: HealthConnectClient): Double {
        try {
            val startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
                .atZone(ZoneId.systemDefault()).toInstant().also { it ->
                    it.minus(1, ChronoUnit.MONTHS)
                }
            val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
            val response =
                healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        WeightRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
            // Get the most recent weight record
            val weight = response.records.lastOrNull()?.weight?.inKilograms
            Log.d("${Consts.LOG_TAG}_HealthConnectService", "readWeight: $weight")
            return weight ?: 0.0
        } catch (e: Exception) {
            Log.e("${Consts.LOG_TAG}_HealthConnectService", "readWeight: ${e.localizedMessage}")
        }
        return 0.0
    }

    override suspend fun readHeight(healthConnectClient: HealthConnectClient): Double {
        try {
            val startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
                .atZone(ZoneId.systemDefault()).toInstant().also { it ->
                    it.minus(1, ChronoUnit.MONTHS)
                }
            val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
            val response =
                healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        HeightRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
            // Get the most recent height record
            val height = response.records.lastOrNull()?.height?.inInches
            Log.d("${Consts.LOG_TAG}_HealthConnectService", "readHeight: $height")
            return height ?: 0.0
        } catch (e: Exception) {
            Log.e("${Consts.LOG_TAG}_HealthConnectService", "readHeight: ${e.localizedMessage}")
        }
        return 0.0
    }

    override suspend fun readSleepSessionOfToday(healthConnectClient: HealthConnectClient): String {
        val startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
            .atZone(ZoneId.systemDefault()).toInstant()
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
        try {
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    SleepSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            val sleepSession = response.records.lastOrNull()?.notes
            Log.d(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readSleepSessionOfToday: $sleepSession"
            )
            return sleepSession ?: "No data"
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readSleepSessionOfToday: ${e.localizedMessage}"
            )
            return "No data"
        }
    }

    override suspend fun readExerciseSessionOfToday(healthConnectClient: HealthConnectClient): String {
        val startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
            .atZone(ZoneId.systemDefault()).toInstant()
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
        try {
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    ExerciseSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            val exerciseSession = response.records.lastOrNull()?.notes
            Log.d(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readExerciseSessionOfToday: $exerciseSession"
            )
            return exerciseSession ?: "No data"
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readExerciseSessionOfToday: ${e.localizedMessage}"
            )
            return "No data"
        }
    }
}
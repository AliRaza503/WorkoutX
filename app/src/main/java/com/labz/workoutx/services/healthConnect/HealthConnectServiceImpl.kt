package com.labz.workoutx.services.healthConnect

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.labz.workoutx.exts.Exts.getKey
import com.labz.workoutx.utils.Consts
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class HealthConnectServiceImpl : HealthConnectService {
    override suspend fun readStepsForLast30Days(healthConnectClient: HealthConnectClient): Map<String, Long> {
        val stepsMap = mutableMapOf<String, Long>()
        try {
            val endTime = LocalDateTime.now()
            var startTime = endTime.minus(30, ChronoUnit.DAYS)
            Log.d(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readStepsForLast30Days: startTime: $startTime endTime: $endTime"
            )
            while (startTime <= endTime) {
                val responseOfADay = healthConnectClient.aggregateGroupByDuration(
                    AggregateGroupByDurationRequest(
                        metrics = setOf(StepsRecord.COUNT_TOTAL),
                        timeRangeFilter = TimeRangeFilter.between(
                            startTime,
                            startTime.plus(1, ChronoUnit.DAYS)
                        ),
                        timeRangeSlicer = Duration.ofMinutes(1L)
                    )
                )
                startTime = startTime.plus(1, ChronoUnit.DAYS)
                val key = startTime.getKey()
                for (durationResult in responseOfADay) {
                    // The result may be null if no data is available in the time range
                    val totalSteps = durationResult.result[StepsRecord.COUNT_TOTAL]
                    if (totalSteps != null) {
                        Log.d(
                            "${Consts.LOG_TAG}_HealthConnectService",
                            "readStepsForLast30Days: $key $totalSteps"
                        )
                        stepsMap[key] = stepsMap.getOrDefault(key, 0L) + totalSteps.toLong()
                    }
                }
            }
            Log.d("${Consts.LOG_TAG}_HealthConnectService", "readStepsForLast30Days: $stepsMap")
            return stepsMap
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readStepsForLast30Days: ${e.localizedMessage}"
            )
        }
        return stepsMap
    }

    override suspend fun readTotalCaloriesBurnedForLast30Days(healthConnectClient: HealthConnectClient): Map<String, Double> {
        val totalCaloriesMap = mutableMapOf<String, Double>()
        try {
            val endTime = LocalDateTime.now()
            var startTime = endTime.minus(30, ChronoUnit.DAYS)
            Log.d(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readTotalCaloriesBurnedForLast30Days: startTime: $startTime endTime: $endTime"
            )
            while (startTime <= endTime) {
                val responseOfADay = healthConnectClient.aggregateGroupByDuration(
                    AggregateGroupByDurationRequest(
                        metrics = setOf(TotalCaloriesBurnedRecord.ENERGY_TOTAL),
                        timeRangeFilter = TimeRangeFilter.between(
                            startTime,
                            startTime.plus(1, ChronoUnit.DAYS)
                        ),
                        timeRangeSlicer = Duration.ofMinutes(1L)
                    )
                )
                startTime = startTime.plus(1, ChronoUnit.DAYS)
                val key = startTime.getKey()
                for (durationResult in responseOfADay) {
                    // The result may be null if no data is available in the time range
                    val totalCalories =
                        durationResult.result[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories
                            ?: 0.0
                    Log.d(
                        "${Consts.LOG_TAG}_HealthConnectService",
                        "readTotalCaloriesBurnedForLast30Days: $key $totalCalories"
                    )
                    totalCaloriesMap[key] =
                        totalCaloriesMap.getOrDefault(key, 0.0) + totalCalories.toDouble()
                }
            }
            Log.d(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readTotalCaloriesBurnedForLast30Days: $totalCaloriesMap"
            )
            return totalCaloriesMap
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readTotalCaloriesBurnedForLast30Days: ${e.localizedMessage}"
            )
        }
        return totalCaloriesMap
    }
}
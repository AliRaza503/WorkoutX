package com.labz.workoutx.services.healthConnect

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SleepSessionRecord.Stage
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.labz.workoutx.utils.Consts
import com.labz.workoutx.exts.Exts.getKey
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
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
                startTime = startTime.plus(1, ChronoUnit.DAYS)
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
            val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
            val startTime = endTime.minus(30, ChronoUnit.DAYS)
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    TotalCaloriesBurnedRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            for (totalCaloriesRecord in response.records) {
                totalCaloriesMap[startTime.toString()] = totalCaloriesRecord.energy.inCalories
                startTime.plus(1, ChronoUnit.DAYS)
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

    override suspend fun readActiveCaloriesBurnedForLast30Days(healthConnectClient: HealthConnectClient): Map<String, Double> {
        val activeCaloriesMap = mutableMapOf<String, Double>()
        try {
            val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
            val startTime = endTime.minus(30, ChronoUnit.DAYS)
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    ActiveCaloriesBurnedRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            for (activeCaloriesRecord in response.records) {
                activeCaloriesMap[startTime.toString()] = activeCaloriesRecord.energy.inCalories
                startTime.plus(1, ChronoUnit.DAYS)
            }
            Log.d(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readActiveCaloriesBurnedForLast30Days: $activeCaloriesMap"
            )
            return activeCaloriesMap
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readActiveCaloriesBurnedForLast30Days: ${e.localizedMessage}"
            )
        }
        return activeCaloriesMap
    }

    override suspend fun readSleepSessionsForLast30Days(healthConnectClient: HealthConnectClient): Map<String, List<Stage>> {
        val sleepSessionsMap = mutableMapOf<String, List<Stage>>()
        try {
            val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
            val startTime = endTime.minus(30, ChronoUnit.DAYS)
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    SleepSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            for (sleepSessionRecord in response.records) {
                sleepSessionsMap[startTime.toString()] = sleepSessionRecord.stages
                startTime.plus(1, ChronoUnit.DAYS)
            }
            Log.d(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readSleepSessionsForLast30Days: $sleepSessionsMap"
            )
            return sleepSessionsMap
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readSleepSessionsForLast30Days: ${e.localizedMessage}"
            )
        }
        return sleepSessionsMap
    }

    override suspend fun readWeight(healthConnectClient: HealthConnectClient): Map<String, Double> {
        val weightMap = mutableMapOf<String, Double>()
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
            for (weightRecord in response.records) {
                weightMap[startTime.toString()] = weightRecord.weight.inKilograms
                startTime.plus(1, ChronoUnit.DAYS)
            }
            Log.d("${Consts.LOG_TAG}_HealthConnectService", "readWeight: $weightMap")
            return weightMap
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_HealthConnectService",
                "readWeight: ${e.localizedMessage}"
            )
            return weightMap
        }
    }
}
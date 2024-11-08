package com.labz.workoutx.viewmodels

import android.app.Application
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.exts.Exts.getRange
import com.labz.workoutx.exts.ProgressTab
import com.labz.workoutx.models.ProgressData
import com.labz.workoutx.models.ProgressData.TargetProgressData
import com.labz.workoutx.services.healthConnect.HealthConnectService
import com.labz.workoutx.uistates.ProgressesUiState
import com.labz.workoutx.exts.Exts.getKey
import com.labz.workoutx.models.Goal
import com.labz.workoutx.models.User
import com.labz.workoutx.services.db.DBService
import com.labz.workoutx.services.goalPredictor.GoalPredictorService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ProgressesViewModel @Inject constructor(
    private val application: Application,
    private val dbService: DBService,
    private val goalPredictorService: GoalPredictorService,
    private val healthConnectService: HealthConnectService
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProgressesUiState())
    val uiState: StateFlow<ProgressesUiState> = _uiState.asStateFlow()

    private val _progressMap = MutableStateFlow<Map<String, ProgressData>>(emptyMap())
    val progressMap: StateFlow<Map<String, ProgressData>> = _progressMap.asStateFlow()

    // The selected tab (Steps, Calories, etc.)
    private val _selectedTab = MutableStateFlow(ProgressTab.STEPS)
    val selectedTab: StateFlow<ProgressTab> = _selectedTab.asStateFlow()

    private val healthConnectClient: HealthConnectClient =
        HealthConnectClient.getOrCreate(application.applicationContext)

    init {
        fetchProgresses()
    }

    // Function to fetch the data
    private fun fetchProgresses() {
        viewModelScope.launch {
            // Set loading to true before fetching the data
            _uiState.value = _uiState.value.copy(isCircularProgressIndicatorVisible = true)
            try {
                // Fetch all data from the service
                val steps = healthConnectService.readStepsForLast30Days(healthConnectClient)
                val totalCalories =
                    healthConnectService.readTotalCaloriesBurnedForLast30Days(healthConnectClient)
                val minutesActivePerDay =
                    dbService.getActivityMinutesForMonth()

                // Create ProgressData objects and store them in the progressMap
                val progresses = mutableMapOf<String, ProgressData>()
                steps.forEach { (date, stepCount) ->
                    progresses[date] = ProgressData(
                        stepsData = stepCount,
                        caloriesBurned = totalCalories[date] ?: 0.0,
                        minutesActive = minutesActivePerDay[date] ?: 0.0,
                    )
                }
                // Update the progress map in the ViewModel
                _progressMap.value = progresses
                loadCurrentWeekProgress()
                // Stop the loading spinner
                _uiState.value = _uiState.value.copy(isCircularProgressIndicatorVisible = false)
            } catch (_: Exception) {
                // Handle any errors and stop loading
                _uiState.value = _uiState.value.copy(isCircularProgressIndicatorVisible = false)
            }
        }
    }

    private fun loadCurrentWeekProgress() {
        // Get the current week's progress
        val progresses = arrayOfNulls<ProgressData>(7)
        // The week starts with Sunday and ends with Saturday
        // Get the data from the last Sunday to the current day of the week and append 0 values for the remaining days till Saturday
        val now = LocalDateTime.now()
        val sunday = now.with(DayOfWeek.SUNDAY).minusDays(7)
        for (i in 0..6) {
            val dayInstant = sunday.plus(i.toLong(), ChronoUnit.DAYS)
            Log.d(
                "ProgressesComposable",
                "loadCurrentWeekProgress: ${progressMap.value[dayInstant.getKey()]}"
            )
            progresses[i] = progressMap.value[dayInstant.getKey()] ?: ProgressData(
                stepsData = 0,
                caloriesBurned = 0.0,
                minutesActive = 0.0,
            )
        }
        _uiState.update {
            it.copy(
                oneWeekData = progresses,
                dateRange = sunday.getRange(),
                sunday = sunday,
                saturday = sunday.plusDays(6),
                dataIsOfCurrentWeek = progressMap.value.containsKey(sunday.plusDays(7).getKey())
                    .not(),
                dataIsOfVeryLastWeek = progressMap.value.containsKey(sunday.getKey()).not()
            )
        }
    }

    fun loadPreviousWeekProgress() {
        val progress = arrayOfNulls<ProgressData>(7)
        val sunday = uiState.value.sunday?.minusDays(7) ?: return
        for (i in 0..6) {
            val dayInstant = sunday.plus(i.toLong(), ChronoUnit.DAYS)
            progress[i] = progressMap.value[dayInstant.getKey()] ?: ProgressData(
                stepsData = 0,
                caloriesBurned = 0.0,
                minutesActive = 0.0,
            )
        }
        _uiState.update {
            it.copy(
                oneWeekData = progress,
                dateRange = sunday.getRange(),
                sunday = sunday,
                saturday = sunday.plusDays(6),
                dataIsOfCurrentWeek = progressMap.value.containsKey(sunday.plusDays(7).getKey())
                    .not(),
                dataIsOfVeryLastWeek = progressMap.value.containsKey(sunday.getKey()).not()
            )
        }
    }

    fun loadNextWeekProgress() {
        // Get the current week's progress
        val progress = arrayOfNulls<ProgressData>(7)
        val sunday = uiState.value.sunday?.plusDays(7) ?: return
        for (i in 0..6) {
            val dayInstant = sunday.plus(i.toLong(), ChronoUnit.DAYS)
            progress[i] = progressMap.value[dayInstant.getKey()] ?: ProgressData(
                stepsData = 0,
                caloriesBurned = 0.0,
                minutesActive = 0.0,
            )
            Log.d(
                "ProgressesComposable",
                "loadNextWeekProgress: ${progressMap.value[dayInstant.getKey()]}"
            )
        }
        _uiState.update {
            it.copy(
                oneWeekData = progress,
                dateRange = sunday.getRange(),
                sunday = sunday,
                saturday = sunday.plusDays(6),
                dataIsOfCurrentWeek = progressMap.value.containsKey(sunday.plusDays(7).getKey())
                    .not(),
                dataIsOfVeryLastWeek = progressMap.value.containsKey(sunday.getKey()).not()
            )
        }
    }

    fun onTabSelected(tabIndex: Int) {
        val tab = ProgressTab.entries[tabIndex]
        _selectedTab.value = tab
        _uiState.update {
            it.copy(
                selectedTabIndex = tab
            )
        }
    }

    fun getProgressPercentage(progress: Float): Float {
        return when (selectedTab.value) {
            ProgressTab.STEPS -> (progress / TargetProgressData.TARGET_STEPS) * 100
            ProgressTab.CALORIES -> (progress / TargetProgressData.TARGET_TOTAL_CALORIES_BURNED) * 100
            ProgressTab.MINUTES -> (progress / TargetProgressData.TARGET_MINUTES_ACTIVE_PER_DAY) * 100
            else -> 0f
        }
    }

    fun getTarget(tab: ProgressTab): String {
        return when (tab) {
            ProgressTab.STEPS -> "to achieve ${TargetProgressData.TARGET_STEPS.toInt()} step count"
            ProgressTab.CALORIES -> "to burn ${TargetProgressData.TARGET_TOTAL_CALORIES_BURNED.toInt()} calories in total"
            ProgressTab.MINUTES -> "to do workout for ${TargetProgressData.TARGET_MINUTES_ACTIVE_PER_DAY.toInt()} minutes per day"
        }
    }

    fun getGoal(): Goal {
        var goal: Goal? = User.goal
        if (goal == null) {
            val avgTotalCaloriesBurned = progressMap.value.values.map { it.caloriesBurned }.average()
            val avgMinutesActive = progressMap.value.values.map { it.minutesActive }.average()
            goalPredictorService.preProcessData(application = application, avgCalories = avgTotalCaloriesBurned, avgMinutes = avgMinutesActive)
            goal = goalPredictorService.predictGoal()
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    dbService.setGoal(goal)
                }
            }
        }
        User.updateGoal(goal)
        return goal
    }

}
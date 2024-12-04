package com.labz.workoutx.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.models.WorkoutTypes
import com.labz.workoutx.services.db.DBService
import com.labz.workoutx.uistates.PerformWorkoutUiState
import com.labz.workoutx.utils.Consts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PerformWorkoutViewModel @Inject constructor(
    private val dbService: DBService
) : ViewModel() {
    private val _uiState = MutableStateFlow(PerformWorkoutUiState())
    val uiState = _uiState.asStateFlow()
    private var workoutReps = 0

    fun loadWorkout(workoutID: String) {
        _uiState.value = uiState.value.copy(isCircularProgressIndicatorVisible = true)
        // Load workout based on workoutID
        val workout = WorkoutTypes.getWorkoutById(workoutID)
        Log.d(
            "${Consts.LOG_TAG}_PerformWorkoutViewModel",
            "loadWorkout: Loaded workout: $workout"
        )
        _uiState.update {
            it.copy(workout = workout)
        }
        _uiState.value =
            uiState.value.copy(isCircularProgressIndicatorVisible = false, isWorkoutLoaded = true)
        startInitialCountdown()
    }

    private fun startInitialCountdown() {
        viewModelScope.launch {
            for (i in 3 downTo 1) {
                _uiState.update { it.copy(countdownValue = i) }
                delay(1000L)
            }
            _uiState.update { it.copy(countdownValue = null) }
            startWorkoutCountdown()
        }
    }

    private fun startWorkoutCountdown() {
        viewModelScope.launch {
            val workoutTargetMinutes = uiState.value.workout?.targetMinutes?.toLong() ?: 0L
            val workoutDuration = workoutTargetMinutes * 60 * 1000
            if (workoutDuration == 0L) {
                Log.e(
                    "${Consts.LOG_TAG} PerformWorkoutViewModel",
                    "startWorkoutCountdown: Invalid workout duration"
                )
                return@launch
            }
//            for (timeLeft in workoutDuration downTo 0 step 1000) {
//                _uiState.update { it.copy(remainingTime = timeLeft) }
//                delay(1000L)
//            }
            var timeLeft = workoutDuration
            while (timeLeft >= 0) {
                if (!uiState.value.isTimerRunning) {
                    delay(1000L)
                    continue
                }
                _uiState.update { it.copy(remainingTime = timeLeft) }
                delay(1000L)
                timeLeft -= 1000
            }
        }
    }

    fun onPlayPauseClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isTimerRunning = !uiState.value.isTimerRunning) }
        }
    }

    fun workoutCompleted() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbService.addActivityMinutesOfToday(
                    minutes = uiState.value.workout?.targetMinutes?.toDouble() ?: 0.0
                )
                dbService.addWorkoutToHistory(uiState.value.workout?.id?.toString() ?: "", workoutReps)
            }
        }
    }

    fun showGetRepsDialogBox() {
        _uiState.update { it.copy(getRepsDialogBoxVisible = true) }
    }

    fun hideGetRepsDialogBox() {
        _uiState.update { it.copy(getRepsDialogBoxVisible = false) }
    }

    fun onRepsEntered(reps: Int) {
        workoutReps = reps
        hideGetRepsDialogBox()
    }
}
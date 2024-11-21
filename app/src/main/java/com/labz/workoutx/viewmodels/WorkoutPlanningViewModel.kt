package com.labz.workoutx.viewmodels

import androidx.lifecycle.ViewModel
import com.labz.workoutx.models.WorkoutTypes
import com.labz.workoutx.uistates.WorkoutPlanningUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WorkoutPlanningViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutPlanningUiState())
    val uiState = _uiState.asStateFlow()

    fun loadWorkoutBasedOnUserGoal(workoutId: String) {
        _uiState.value = uiState.value.copy(isCircularProgressIndicatorVisible = true)
        // Load workout based on user goal
        _uiState.update {
            it.copy(workout = WorkoutTypes.getWorkoutById(id = workoutId))
        }
        _uiState.value = uiState.value.copy(isCircularProgressIndicatorVisible = false)
    }

    fun activityMinutesChanged(minutes: Int) {
        if (minutes < 1) return
        _uiState.update {
            it.copy(activityMinutes = minutes)
        }
    }

    fun setWorkoutMinutes() {
        WorkoutTypes.setWorkoutMinutes(uiState.value.workout!!, uiState.value.activityMinutes)
    }
}
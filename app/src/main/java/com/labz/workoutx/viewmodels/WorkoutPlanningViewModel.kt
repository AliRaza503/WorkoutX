package com.labz.workoutx.viewmodels

import androidx.lifecycle.ViewModel
import com.labz.workoutx.models.Goal
import com.labz.workoutx.models.User
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

    init {
        loadWorkoutBasedOnUserGoal(User.goal)
    }

    private fun loadWorkoutBasedOnUserGoal(goal: Goal?) {
        _uiState.value = uiState.value.copy(isCircularProgressIndicatorVisible = true)
        // Load workout based on user goal
        _uiState.update {
            it.copy(workout = WorkoutTypes.getRandomWorkout(goal = goal ?: Goal.MAINTAIN_WEIGHT))
        }
        _uiState.value = uiState.value.copy(isCircularProgressIndicatorVisible = false)
    }
}
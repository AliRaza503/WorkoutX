package com.labz.workoutx.viewmodels

import androidx.lifecycle.ViewModel
import com.labz.workoutx.models.Goal
import com.labz.workoutx.models.User
import com.labz.workoutx.models.WorkoutTypes
import com.labz.workoutx.uistates.ChooseWorkoutUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChooseWorkoutViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ChooseWorkoutUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAllWorkouts()
        filterWorkoutsListForGoal(User.goal)
    }

    private fun loadAllWorkouts() {
        _uiState.value = uiState.value.copy(isCircularProgressIndicatorVisible = true)
        // Load all workouts
        _uiState.update {
            it.copy(allWorkouts = WorkoutTypes.getAllWorkouts())
        }
        _uiState.value = uiState.value.copy(isCircularProgressIndicatorVisible = false)
    }

    fun filterWorkoutsListForGoal(goal: Goal?) {
        _uiState.value = uiState.value.copy(isCircularProgressIndicatorVisible = true)
        if (goal != null) {
            if (_uiState.value.selectedGoalsList.contains(goal) && _uiState.value.selectedGoalsList.size > 1) {
                _uiState.update { state ->
                    state.copy(
                        selectedGoalsList = state.selectedGoalsList - goal,
                    )
                }
                // Update the filtered workouts list based on the selected goals list using allWorkouts map
                _uiState.update { state ->
                    state.copy(
                        filteredWorkouts = state.selectedGoalsList.flatMap { state.allWorkouts[it].orEmpty() }
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        selectedGoalsList = state.selectedGoalsList + goal,
                        filteredWorkouts = state.filteredWorkouts + state.allWorkouts[goal].orEmpty()
                    )
                }
            }
        }
        _uiState.value = uiState.value.copy(isCircularProgressIndicatorVisible = false)
    }

    fun isSelectedGoal(goal: Goal): Boolean {
        return _uiState.value.selectedGoalsList.contains(goal)
    }
}
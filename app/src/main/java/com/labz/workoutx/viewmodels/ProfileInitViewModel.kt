package com.labz.workoutx.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.models.Gender
import com.labz.workoutx.models.Goal
import com.labz.workoutx.services.db.DBService
import com.labz.workoutx.uistates.ProfileInitUiState
import com.labz.workoutx.utils.Consts
import com.labz.workoutx.utils.DateFormatters.toFormattedDateString
import com.labz.workoutx.utils.DateFormatters.toFormattedString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class ProfileInitViewModel @Inject constructor(
    private val dbService: DBService
) : ViewModel() {
    var uiState = mutableStateOf(ProfileInitUiState())
        private set
    private val _navigateToDashboard = MutableStateFlow(false)
    val navigateToDashboard: StateFlow<Boolean> = _navigateToDashboard

    fun onGenderChanged(gender: String) {
        try {
            uiState.value =
                uiState.value.copy(gender = Gender.valueOf(gender = gender), genderError = "")
        } catch (e: IllegalArgumentException) {
            Log.e("${Consts.LOG_TAG}_ProfileInitViewModel", "onGenderChanged: ${e.message}")
            uiState.value = uiState.value.copy(gender = null, genderError = "Invalid gender")
        }
    }

    fun onDateOfBirthChanged(dateOfBirth: String) {
        uiState.value = uiState.value.copy(
            dateOfBirth = dateOfBirth.toFormattedDateString(),
            dateOfBirthError = null
        )
    }

    fun onDateOfBirthChanged(dateOfBirth: Long?) {
        if (dateOfBirth == null) {
            Log.e("${Consts.LOG_TAG}_ProfileInitViewModel", "onDateOfBirthChanged: Invalid date")
            uiState.value = uiState.value.copy(
                dateOfBirthError = "Invalid date"
            )
            return
        }
        val calendar = Calendar.getInstance().apply {
            timeInMillis = dateOfBirth
        }
        val dateOfBirth = calendar.toFormattedString()
        uiState.value = uiState.value.copy(
            dateOfBirth = dateOfBirth
        )
        Log.d(
            "${Consts.LOG_TAG}_ProfileInitViewModel",
            "onDateOfBirthChange: ${uiState.value.dateOfBirth}"
        )
    }

    fun onWeightChanged(value: String) {
        if (value.isEmpty() || value.toDoubleOrNull() == null) {
            uiState.value = uiState.value.copy(
                weightError = "Weight is required",
                weightInKgs = ""
            )
            Log.d("${Consts.LOG_TAG}_ProfileInitViewModel", "onWeightChanged: Invalid value")
        }
        try {
            val doubleValue =
                value.toDouble() // This will throw a NumberFormatException if the value is not a valid double
            if (doubleValue < 0) {
                uiState.value = uiState.value.copy(
                    weightError = "Weight cannot be negative",
                    weightInKgs = ""
                )
                Log.e("${Consts.LOG_TAG}_ProfileInitViewModel", "onWeightChanged: Negative value")
                return
            }
            uiState.value = uiState.value.copy(
                weightInKgs = value,
                weightError = null
            )
        } catch (e: NumberFormatException) {
            uiState.value = uiState.value.copy(
                weightError = "Invalid weight!\nPlease enter a decimal number",
                weightInKgs = ""
            )
            Log.e("${Consts.LOG_TAG}_ProfileInitViewModel", "onWeightChanged: ${e.message}")
        }
    }

    fun onHeightChanged(value: String) {
        if (value.isEmpty() || value.toDoubleOrNull() == null) {
            uiState.value = uiState.value.copy(
                heightError = "Height is required",
                heightInCms = ""
            )
            Log.d("${Consts.LOG_TAG}_ProfileInitViewModel", "onHeightChanged: Invalid value")
        }
        try {
            val doubleValue =
                value.toDouble() // This will throw a NumberFormatException if the value is not a valid double
            if (doubleValue < 0) {
                uiState.value = uiState.value.copy(
                    heightError = "Height cannot be negative",
                    heightInCms = ""
                )
                Log.e("${Consts.LOG_TAG}_ProfileInitViewModel", "onHeightChanged: Negative value")
                return
            }
            uiState.value = uiState.value.copy(
                heightInCms = value,
                heightError = null
            )
        } catch (e: NumberFormatException) {
            uiState.value = uiState.value.copy(
                heightError = "Invalid height\nPlease enter a decimal number",
                heightInCms = ""
            )
            Log.e("${Consts.LOG_TAG}_ProfileInitViewModel", "onHeightChanged: ${e.message}")
        }
    }

    fun onGoalChanged(goal: String) {
        try {
            uiState.value = uiState.value.copy(goal = Goal.valueOf(goal = goal))
        } catch (e: IllegalArgumentException) {
            Log.e("${Consts.LOG_TAG}_ProfileInitViewModel", "onGoalChanged: ${e.message}")
            uiState.value = uiState.value.copy(goalError = "Invalid goal")
        }
    }

    fun openDatePickerForResult() {
        uiState.value = uiState.value.copy(datePickerOpened = true)
    }

    fun genderDropDownChanged(isExpanded: Boolean = false) {
        uiState.value = uiState.value.copy(genderDropDownOpened = isExpanded)
    }

    fun goalDropDownChanged(isExpanded: Boolean) {
        uiState.value = uiState.value.copy(goalDropDownOpened = isExpanded)
    }

    fun closeDatePicker() {
        uiState.value = uiState.value.copy(datePickerOpened = false)
    }

    fun nextBtnClicked() {
        uiState.value = uiState.value.copy(
            weightError = if (uiState.value.weightInKgs.isNullOrEmpty() == true) "Weight is required" else null,
            heightError = if (uiState.value.heightInCms.isNullOrEmpty() == true) "Height is required" else null,
            dateOfBirthError = if (uiState.value.dateOfBirth.isNullOrEmpty() == true) "Date of birth is required" else null,
            genderError = if (uiState.value.gender == null) "Gender is required" else null,
            goalError = if (uiState.value.goal == null) "Goal is required" else null
        )
        if (!uiState.value.hasErrors()) {
            viewModelScope.launch {
                dbService.setUserData(
                    weightInKgs = uiState.value.weightInKgs!!.toDouble(),
                    heightInCms = uiState.value.heightInCms!!.toDouble(),
                    dateOfBirth = uiState.value.dateOfBirth!!,
                    gender = uiState.value.gender!!.toString(),
                    goal = uiState.value.goal!!.toString()
                )
            }
            _navigateToDashboard.value = true
        } else {
            Log.e("${Consts.LOG_TAG}_ProfileInitViewModel", "nextBtnClicked: Has errors")
        }
    }

    fun onNavigated() {
        _navigateToDashboard.value = false // Reset navigation state after navigating
    }
}


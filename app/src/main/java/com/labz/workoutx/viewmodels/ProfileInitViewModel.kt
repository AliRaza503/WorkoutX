package com.labz.workoutx.viewmodels

import android.util.Log
import android.util.Log.e
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.labz.workoutx.models.Gender
import com.labz.workoutx.models.Goal
import com.labz.workoutx.models.User.dateOfBirth
import com.labz.workoutx.uistates.ProfileInitUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class ProfileInitViewModel @Inject constructor() : ViewModel() {
    var uiState = mutableStateOf(ProfileInitUiState())
        private set

    fun onGenderChanged(gender: String) {
        try {
            uiState.value = uiState.value.copy(gender = Gender.valueOf(gender = gender))
        } catch (e: IllegalArgumentException) {
            Log.e("ProfileInitViewModel", "onGenderChanged: ${e.message}")
            uiState.value = uiState.value.copy(genderError = "Invalid gender")
        }
    }

    fun onDateOfBirthChanged(dateOfBirth: String) {
        uiState.value = uiState.value.copy(
            dateOfBirth = Calendar.getInstance().apply {
                time =
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateOfBirth) ?: Date()
                Log.d("ProfileInitViewModel", "onDateOfBirthChanged: $time")
            }.toString()
        )
    }

    fun onDateOfBirthChanged(dateOfBirth: Long?) {
        if (dateOfBirth == null) {
            Log.e("ProfileInitViewModel", "onDateOfBirthChanged: Invalid date")
            uiState.value = uiState.value.copy(
                dateOfBirthError = "Invalid date"
            )
            return
        }
        val calendar = Calendar.getInstance().apply {
            timeInMillis = dateOfBirth
        }
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val dateOfBirth = "$date/$month/$year"
        uiState.value = uiState.value.copy(
            dateOfBirth = dateOfBirth
        )
        Log.d("ProfileInitViewModel", "onDateOfBirthChange: ${uiState.value.dateOfBirth}")
    }

    fun onWeightChanged(value: String) {
        if (value.isEmpty() || value.toDoubleOrNull() == null) {
            uiState.value = uiState.value.copy(
                weightError = "Weight is required",
                weightInKgs = ""
            )
            Log.d("ProfileInitViewModel", "onWeightChanged: Invalid value")
        }
        try {
            val doubleValue =
                value.toDouble() // This will throw a NumberFormatException if the value is not a valid double
            if (doubleValue < 0) {
                uiState.value = uiState.value.copy(
                    weightError = "Weight cannot be negative",
                    weightInKgs = ""
                )
                Log.e("ProfileInitViewModel", "onWeightChanged: Negative value")
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
            Log.e("ProfileInitViewModel", "onWeightChanged: ${e.message}")
        }
    }

    fun onHeightChanged(value: String) {
        if (value.isEmpty() || value.toDoubleOrNull() == null) {
            uiState.value = uiState.value.copy(
                heightError = "Height is required",
                heightInCms = ""
            )
            Log.d("ProfileInitViewModel", "onHeightChanged: Invalid value")
        }
        try {
            val doubleValue =
                value.toDouble() // This will throw a NumberFormatException if the value is not a valid double
            if (doubleValue < 0) {
                uiState.value = uiState.value.copy(
                    heightError = "Height cannot be negative",
                    heightInCms = ""
                )
                Log.e("ProfileInitViewModel", "onHeightChanged: Negative value")
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
            Log.e("ProfileInitViewModel", "onHeightChanged: ${e.message}")
        }
    }

    fun onGoalChanged(goal: String) {
        try {
            uiState.value = uiState.value.copy(goal = Goal.valueOf(goal = goal))
        } catch (e: IllegalArgumentException) {
            Log.e("ProfileInitViewModel", "onGoalChanged: ${e.message}")
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
}


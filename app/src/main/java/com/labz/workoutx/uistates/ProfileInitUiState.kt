package com.labz.workoutx.uistates

import com.labz.workoutx.models.Gender
import com.labz.workoutx.models.Goal

data class ProfileInitUiState(
    val gender: Gender? = null,
    val dateOfBirth: String? = null,
    val weightInKgs: String? = null,
    val heightInCms: String? = null,
    val goal: Goal? = null,
    val datePickerOpened: Boolean = false,
    val genderDropDownOpened: Boolean = false,
    val goalDropDownOpened: Boolean = false,
    val weightError: String? = null,
    val heightError: String? = null,
    val genderError: String? = null,
    val goalError: String? = null,
    val dateOfBirthError: String? = null
) {
    fun hasErrors(): Boolean = weightError != null
            || heightError != null
            || genderError != null
            || goalError != null
            || dateOfBirthError != null
}

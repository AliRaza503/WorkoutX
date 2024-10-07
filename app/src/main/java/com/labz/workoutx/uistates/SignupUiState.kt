package com.labz.workoutx.uistates

data class SignupUiState (
    val email: String = "",
    val password: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val isPasswordVisible: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val firstNameError: String = "",
    val lastNameError: String = "",
    var isLoadingAcc: Boolean = false,
    var showToast : Boolean = false,
    var toastMessage : String? = null
)
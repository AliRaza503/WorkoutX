package com.labz.workoutx.uistates

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    var isLoadingAcc: Boolean = false,
    var showToast : Boolean = false,
    var toastMessage : String? = null
)
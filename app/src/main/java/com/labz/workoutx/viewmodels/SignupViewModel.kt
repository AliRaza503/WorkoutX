package com.labz.workoutx.viewmodels

import android.content.Context
import android.util.Log
import android.util.Log.e
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.services.auth.AccountService
import com.labz.workoutx.throwables.SignupExceptions
import com.labz.workoutx.uistates.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {
    var uiState = mutableStateOf(SignupUiState())
        private set

    private fun getEmailError(value: String) =
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(value)
                .matches()
        ) "" else if (value.isBlank()) "Email is required" else "Invalid email"

    private fun getFirstNameError(value: String) =
        if (value.isBlank()) "First name is required" else ""

    private fun getLastNameError(value: String) =
        if (value.isBlank()) "Last name is required" else ""

    private fun getPasswordError(value: String) = when {
        value.isBlank() -> "Password is required"
        value.length < 8 -> "Password must be 8 characters long"
        !value.contains(Regex("[A-Z]")) -> "Password must contain at least one uppercase letter"
        !value.contains(Regex("[a-z]")) -> "Password must contain at least one lowercase letter"
        !value.contains(Regex("[0-9]")) -> "Password must contain at least one number"
        !value.contains(Regex("[^A-Za-z0-9]")) -> "Password must contain at least one special character"
        else -> "" // Valid password
    }

    fun onEmailChanged(email: String) {
        uiState.value = uiState.value.copy(email = email, emailError = getEmailError(email))
    }

    fun onPasswordChanged(password: String) {
        uiState.value =
            uiState.value.copy(password = password, passwordError = getPasswordError(password))
    }

    fun onFirstNameChanged(firstName: String) {
        uiState.value =
            uiState.value.copy(firstName = firstName, firstNameError = getFirstNameError(firstName))
    }

    fun onLastNameChanged(lastName: String) {
        uiState.value =
            uiState.value.copy(lastName = lastName, lastNameError = getLastNameError(lastName))
    }

    fun togglePasswordVisibility() {
        uiState.value = uiState.value.copy(isPasswordVisible = !uiState.value.isPasswordVisible)
    }

    fun hasErrors(): Boolean {
        val emailError = getEmailError(uiState.value.email)
        val passwordError = getPasswordError(uiState.value.password)
        val firstNameError = getFirstNameError(uiState.value.firstName)
        val lastNameError = getLastNameError(uiState.value.lastName)
        uiState.value = uiState.value.copy(
            emailError = emailError,
            passwordError = passwordError,
            firstNameError = firstNameError,
            lastNameError = lastNameError
        )
        return emailError.isNotBlank() || passwordError.isNotBlank() || firstNameError.isNotBlank() || lastNameError.isNotBlank()
    }

    private fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoadingAcc = true)
            try {
                accountService.authenticate(email, password) { result ->
                    if (result == null) {
                        Log.d("SignupViewModel", "authenticate: Success")
                        onResult(null)
                    } else {
                        e("SignupViewModel", "authenticate: Error: $result")
                        throw result
                    }
                }
            } catch (e: SignupExceptions) {
                e("SignupViewModel", "authenticate: ${e.localizedMessage}")
                onResult(e)
            } finally {
                uiState.value = uiState.value.copy(isLoadingAcc = false)
            }
        }
    }

    fun createAccount() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoadingAcc = true)
            try {
                accountService.createAccountWithEmailPassword(
                    uiState.value.email,
                    uiState.value.password,
                    "${uiState.value.firstName} ${uiState.value.lastName}"
                ) { result ->
                    if (result == null) {
                        Log.d("SignupViewModel", "createAccount: Account created successfully")
                        // Now authenticate the user and navigate to home screen
                        authenticate(uiState.value.email, uiState.value.password) { result ->
                            if (result == null) {
                                Log.d(
                                    "SignupViewModel",
                                    "createAccount: Authenticated successfully"
                                )
                                uiState.value = uiState.value.copy(
                                    showToast = true,
                                    toastMessage = "Account created and authenticated successfully"
                                )
                                // TODO: Navigate to home screen
                            }
                        }
                    } else {
                        throw result
                    }
                }
            } catch (e: SignupExceptions) {
                uiState.value = uiState.value.copy(
                    showToast = true,
                    toastMessage = e.localizedMessage
                )
                Log.e("SignupViewModel", "createAccount: ${e.localizedMessage}")
            } finally {
                uiState.value = uiState.value.copy(isLoadingAcc = false)
            }
        }
    }

    fun disableToast() {
        uiState.value = uiState.value.copy(showToast = false, toastMessage = null)
    }

    fun signInWithGoogle(context: Context) {
        uiState.value = uiState.value.copy(isLoadingAcc = true)
        viewModelScope.launch {
            try {
                accountService.signInWithGoogle(context).collect { result ->
                    result.fold(
                        onSuccess = {
                            Log.d("LoginViewModel", "signInWithGoogle: Success")
                            uiState.value = uiState.value.copy(
                                showToast = true,
                                toastMessage = "Login Successful",
                                isLoadingAcc = false
                            )
                            // TODO: Navigate to home screen
                        },
                        onFailure = { e ->
                            Log.d("LoginViewModel", "signInWithGoogle: ${e.localizedMessage}")
                            uiState.value = uiState.value.copy(
                                showToast = true,
                                toastMessage = "Failed to sign in with Google",
                                isLoadingAcc = false
                            )
                        }
                    )
                }
            } finally {
                uiState.value = uiState.value.copy(isLoadingAcc = false)
            }
        }
    }
}
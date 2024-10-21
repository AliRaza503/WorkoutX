package com.labz.workoutx.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.services.auth.AccountService
import com.labz.workoutx.services.db.DBService
import com.labz.workoutx.throwables.LoginException
import com.labz.workoutx.uistates.LoginUiState
import com.labz.workoutx.utils.Consts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    private val dbService: DBService
) : ViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val _authSucceeded = MutableStateFlow(false)
    val authSucceeded: StateFlow<Boolean> = _authSucceeded

    private fun getEmailError(email: String): String? {
        return if (email.isEmpty()) {
            "Email is required"
        } else null
    }

    private fun getPasswordError(password: String): String? {
        return if (password.isEmpty()) {
            "Password is required"
        } else null
    }

    fun onEmailChanged(email: String) {
        uiState.value = uiState.value.copy(email = email, emailError = getEmailError(email))
    }

    fun onPasswordChanged(password: String) {
        uiState.value =
            uiState.value.copy(password = password, passwordError = getPasswordError(password))
    }

    fun togglePasswordVisibility() {
        uiState.value = uiState.value.copy(isPasswordVisible = !uiState.value.isPasswordVisible)
    }

    fun hasErrors(): Boolean {
        val emailError = getEmailError(uiState.value.email)
        val passwordError = getPasswordError(uiState.value.password)
        uiState.value = uiState.value.copy(emailError = emailError, passwordError = passwordError)
        return emailError != null || passwordError != null
    }

    fun authenticate() {
        val email = uiState.value.email
        val password = uiState.value.password
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoadingAcc = true)
            try {
                accountService.authenticate(email, password, dbServiceRef = dbService) { result ->
                    if (result == null) {
                        Log.d("${Consts.LOG_TAG}_LoginViewModel", "authenticate: Success")
                        uiState.value = uiState.value.copy(
                            showToast = true,
                            toastMessage = "Login Successful",
                            isLoadingAcc = false
                        )
                        // Authentication successful, navigate to the next screen
                        _authSucceeded.value = true
                    } else {
                        Log.d(
                            "${Consts.LOG_TAG}_LoginViewModel",
                            "authenticate: ${result.localizedMessage}"
                        )
                        throw result
                    }
                }
            } catch (e: LoginException) {
                uiState.value = uiState.value.copy(
                    showToast = true,
                    toastMessage = e.localizedMessage,
                    isLoadingAcc = false
                )
                Log.d("${Consts.LOG_TAG}_LoginViewModel", "authenticate: ${e.localizedMessage}")
            } finally {
                uiState.value = uiState.value.copy(isLoadingAcc = false)
            }
        }
    }

    fun isEmailEntered(): Boolean {
        return uiState.value.email.isNotEmpty()
    }

    fun sendPasswordResetEmail() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoadingAcc = true)
            try {
                accountService.sendPasswordResetEmail(uiState.value.email)
                Log.d("${Consts.LOG_TAG}_LoginViewModel", "sendPasswordResetEmail: Success")
                uiState.value = uiState.value.copy(
                    showToast = true,
                    toastMessage = "Password reset email sent"
                )
            } catch (e: Exception) {
                Log.d(
                    "${Consts.LOG_TAG}_LoginViewModel",
                    "sendPasswordResetEmail: ${e.localizedMessage}"
                )
                uiState.value = uiState.value.copy(
                    showToast = true,
                    toastMessage = "Failed to send password reset email"
                )
            } finally {
                uiState.value = uiState.value.copy(isLoadingAcc = false)
            }
        }
    }

    fun signInWithGoogle(context: Context) {
        uiState.value = uiState.value.copy(isLoadingAcc = true)
        viewModelScope.launch {
            try {
                accountService.signInWithGoogle(context = context, dbServiceRef = dbService).collect { result ->
                    result.fold(
                        onSuccess = {
                            Log.d("${Consts.LOG_TAG}_LoginViewModel", "signInWithGoogle: Success")
                            uiState.value = uiState.value.copy(
                                showToast = true,
                                toastMessage = "Login Successful",
                                isLoadingAcc = false
                            )
                            // Authentication successful, navigate to the next screen
                            _authSucceeded.value = true
                        },
                        onFailure = { e ->
                            Log.d(
                                "${Consts.LOG_TAG}_LoginViewModel",
                                "signInWithGoogle: ${e.localizedMessage}"
                            )
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

    fun disableToast() {
        uiState.value = uiState.value.copy(showToast = false, toastMessage = null)
    }

    fun onNavigated() {
        _authSucceeded.value = false
    }
}
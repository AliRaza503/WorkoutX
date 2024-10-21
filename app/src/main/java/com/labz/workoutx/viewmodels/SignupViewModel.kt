package com.labz.workoutx.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.services.auth.AccountService
import com.labz.workoutx.services.db.DBService
import com.labz.workoutx.throwables.SignupExceptions
import com.labz.workoutx.uistates.SignupUiState
import com.labz.workoutx.utils.Consts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(
    private val accountService: AccountService,
    private val dbService: DBService
) : ViewModel() {
    var uiState = mutableStateOf(SignupUiState())
        private set

    private val _authSucceeded = MutableStateFlow(false)
    val authSucceeded: StateFlow<Boolean> = _authSucceeded


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
                accountService.authenticate(email, password, dbServiceRef = dbService) { result ->
                    if (result == null) {
                        Log.d("${Consts.LOG_TAG}_SignupViewModel", "authenticate: Success")
                        onResult(null)
                    } else {
                        Log.e("${Consts.LOG_TAG}_SignupViewModel", "authenticate: Error: $result")
                        throw result
                    }
                }
            } catch (e: SignupExceptions) {
                Log.e("${Consts.LOG_TAG}_SignupViewModel", "authenticate: ${e.localizedMessage}")
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
                        Log.d(
                            "${Consts.LOG_TAG}_SignupViewModel",
                            "createAccount: Account created successfully"
                        )
                        // Now authenticate the user and navigate to the next screen
                        authenticate(uiState.value.email, uiState.value.password) { result ->
                            if (result == null) {
                                Log.d(
                                    "${Consts.LOG_TAG}_SignupViewModel",
                                    "createAccount: Authenticated successfully"
                                )
                                uiState.value = uiState.value.copy(
                                    showToast = true,
                                    toastMessage = "Account created and authenticated successfully"
                                )
                                _authSucceeded.value = true
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
                Log.e("${Consts.LOG_TAG}_SignupViewModel", "createAccount: ${e.localizedMessage}")
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
                accountService.signInWithGoogle(context = context, dbServiceRef = dbService).collect { result ->
                    result.fold(
                        onSuccess = {
                            Log.d("LoginViewModel", "signInWithGoogle: Success")
                            uiState.value = uiState.value.copy(
                                showToast = true,
                                toastMessage = "Login Successful",
                                isLoadingAcc = false
                            )
                            _authSucceeded.value = true
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

    fun onNavigated() {
        _authSucceeded.value = false
    }
}
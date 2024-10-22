package com.labz.workoutx.viewmodels

import android.app.Application
import android.os.RemoteException
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.services.auth.AccountService
import com.labz.workoutx.services.healthConnect.HealthConnectService
import com.labz.workoutx.services.permissions.PermissionsService
import com.labz.workoutx.uistates.DashboardUiState
import com.labz.workoutx.uistates.PermissionsUiState
import com.labz.workoutx.utils.Consts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val accountService: AccountService,
    private val permissionsService: PermissionsService,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    private var _isSignOutSucceeded = MutableStateFlow(false)
    val isSignOutSucceeded: StateFlow<Boolean> = _isSignOutSucceeded


    fun signOut() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCircularProgressIndicatorVisible = true) }
            accountService.signOut { result ->
                _uiState.update { it.copy(isCircularProgressIndicatorVisible = false) }
                if (result == null) {
                    _isSignOutSucceeded.value = true
                    Log.d("${Consts.LOG_TAG}_DashboardViewModel", "signOut: Success")
                } else {
                    Log.e(
                        "${Consts.LOG_TAG}_DashboardViewModel",
                        "signOut: ${result.localizedMessage}"
                    )
                }
            }
        }
    }

    fun onNavigated() {
        _isSignOutSucceeded.value = false
    }
}
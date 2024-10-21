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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    application: Application,
    private val accountService: AccountService,
    private val healthConnectService: HealthConnectService,
    private val permissionsService: PermissionsService,
) : ViewModel() {
    private val healthConnectClient: HealthConnectClient
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    private var _isSignOutSucceeded = MutableStateFlow(false)
    val isSignOutSucceeded: StateFlow<Boolean> = _isSignOutSucceeded

    init {
        val context = application.applicationContext
        healthConnectClient = HealthConnectClient.getOrCreate(context)
        initialLoad()
    }


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

    private fun initStepsOfToday() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val stepsRecord = healthConnectService.readTodaySteps(healthConnectClient)
                Log.d("${Consts.LOG_TAG}_DashboardViewModel", "getStepsOfToday: $stepsRecord")
                _uiState.update { it.copy(stepsOfToday = stepsRecord) }
            }
        }
    }

    private fun initHeartRateNow() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val heartRateRecord = healthConnectService.readHeartRateNow(healthConnectClient)
                Log.d(
                    "${Consts.LOG_TAG}_DashboardViewModel",
                    "getHeartRateOfToday: $heartRateRecord"
                )
                _uiState.update { it.copy(heartRateNow = heartRateRecord) }
            }
        }
    }

    private fun initActiveCaloriesBurnedOfToday() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val activeCaloriesBurned =
                    healthConnectService.readActiveCaloriesBurnedToday(healthConnectClient)
                Log.d(
                    "${Consts.LOG_TAG}_DashboardViewModel",
                    "getActiveCaloriesBurnedOfToday: $activeCaloriesBurned"
                )
                _uiState.update { it.copy(activeCaloriesBurnedToday = activeCaloriesBurned) }
            }
        }
    }

    private fun initTotalCaloriesBurnedOfToday() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val totalCaloriesBurned =
                    healthConnectService.readTotalCaloriesBurnedToday(healthConnectClient)
                Log.d(
                    "${Consts.LOG_TAG}_DashboardViewModel",
                    "getTotalCaloriesBurnedOfToday: $totalCaloriesBurned"
                )
                _uiState.update { it.copy(totalCaloriesBurnedToday = totalCaloriesBurned) }
            }
        }
    }

    private fun initExerciseSessionOfToday() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val exerciseSession =
                    healthConnectService.readExerciseSessionOfToday(healthConnectClient)
                Log.d(
                    "${Consts.LOG_TAG}_DashboardViewModel",
                    "getExerciseSessionOfToday: $exerciseSession"
                )
                _uiState.update { it.copy(exerciseSessionOfToday = exerciseSession) }
            }
        }
    }

    private fun initWeight() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val weight = healthConnectService.readWeight(healthConnectClient)
                Log.d("${Consts.LOG_TAG}_DashboardViewModel", "getWeightOfToday: $weight")
                _uiState.update { it.copy(weightInKgs = weight) }
            }
        }
    }

    private fun initHeight() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val height = healthConnectService.readHeight(healthConnectClient)
                Log.d("${Consts.LOG_TAG}_DashboardViewModel", "getHeightOfToday: $height")
                _uiState.update { it.copy(heightInCms = height) }
            }
        }
    }

    private fun initSleepSessionOfToday() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sleepSession = healthConnectService.readSleepSessionOfToday(healthConnectClient)
                Log.d(
                    "${Consts.LOG_TAG}_DashboardViewModel",
                    "getSleepSessionOfToday: $sleepSession"
                )
                _uiState.update { it.copy(sleepSessionOfToday = sleepSession) }
            }
        }
    }

    /**
     * Provides permission check and error handling for Health Connect suspend function calls.
     *
     * Permissions are checked prior to execution of [block], and if all permissions aren't granted
     * the [block] won't be executed, and [_uiState] will be set to false, which will
     * result in the UI showing the permissions button.
     *
     * Where an error is caught, of the type Health Connect is known to throw, [PermissionsUiState] is set to
     * [PermissionsUiState.Error], which results in the snack bar being used to show the error message.
     */
    private suspend fun tryWithPermissionsCheck(block: suspend () -> Unit) {
        allPermissionsGranted()
        try {
            if (_uiState.value.allPermissionsGranted) {
                Log.d(
                    "${Consts.LOG_TAG}_DashboardViewModel",
                    "tryWithPermissionsCheck: Permissions granted"
                )
                block()
            }
            PermissionsUiState.Granted
        } catch (remoteException: RemoteException) {
            PermissionsUiState.Error(remoteException)
        } catch (securityException: SecurityException) {
            PermissionsUiState.Error(securityException)
        } catch (ioException: IOException) {
            PermissionsUiState.Error(ioException)
        } catch (illegalStateException: IllegalStateException) {
            PermissionsUiState.Error(illegalStateException)
        }
    }

    fun initialLoad() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                initStepsOfToday()
                initHeartRateNow()
                initActiveCaloriesBurnedOfToday()
                initTotalCaloriesBurnedOfToday()
                initExerciseSessionOfToday()
                initWeight()
                initHeight()
                initSleepSessionOfToday()
            }
        }
    }

    fun allPermissionsGranted() {
        _uiState.update { it.copy(isCircularProgressIndicatorVisible = true) }
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    allPermissionsGranted = permissionsService.hasAllPermissions(
                        healthConnectClient
                    )
                )
            }
            _uiState.update { it.copy(isCircularProgressIndicatorVisible = false) }
        }
    }
}
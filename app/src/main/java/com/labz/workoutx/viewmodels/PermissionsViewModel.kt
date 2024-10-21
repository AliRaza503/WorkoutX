package com.labz.workoutx.viewmodels

import android.app.Application
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController.Companion.createRequestPermissionResultContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.services.permissions.PermissionsService
import com.labz.workoutx.uistates.PermissionsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Create a hilt view model class named PermissionsViewModel

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    application: Application,
    private val permissionsService: PermissionsService
) : ViewModel() {
    private val healthConnectClient: HealthConnectClient

    init {
        val context = application.applicationContext
        healthConnectClient = HealthConnectClient.getOrCreate(context)
    }

    private val _permissionsUiState =
        MutableStateFlow<PermissionsUiState>(PermissionsUiState.Uninitialized)
    val permissionsUiState: StateFlow<PermissionsUiState> = _permissionsUiState.asStateFlow()

    val permissionsLauncher: ActivityResultContract<Set<String>, Set<String>> =
        createRequestPermissionResultContract()

    fun getPermissionsSet(): Set<String> = permissionsService.getPermissionsSet()
    fun checkPermissions() {
        viewModelScope.launch(Dispatchers.Default) {
            val hasPermissions = permissionsService.hasAllPermissions(healthConnectClient)
            withContext(Dispatchers.Main) {
                _permissionsUiState.value = if (hasPermissions) {
                    PermissionsUiState.Granted
                } else {
                    _permissionsUiState.value
                }
            }
        }
    }
}
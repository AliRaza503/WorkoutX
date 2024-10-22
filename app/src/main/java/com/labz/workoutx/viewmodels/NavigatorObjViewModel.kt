package com.labz.workoutx.viewmodels

import android.app.Application
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.services.auth.AccountService
import com.labz.workoutx.services.db.DBService
import com.labz.workoutx.services.permissions.PermissionsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NavigatorObjViewModel @Inject constructor(
    application: Application,
    private val permissionsService: PermissionsService,
    private val accountService: AccountService,
    private val dbService: DBService
) : ViewModel() {
    private val _isUserInfoLoaded = MutableStateFlow<Boolean?>(null)
    val isUserInfoLoaded: StateFlow<Boolean?> = _isUserInfoLoaded

    private val _areAllPermissionsGranted = MutableStateFlow<Boolean?>(null)
    val areAllPermissionsGranted: StateFlow<Boolean?> = _areAllPermissionsGranted

    private val healthConnectClient: HealthConnectClient

    init {
        val context = application.applicationContext
        healthConnectClient = HealthConnectClient.getOrCreate(context)
        checkIfUserInfoLoaded()
        viewModelScope.launch {
            _areAllPermissionsGranted.value =
                permissionsService.hasAllPermissions(healthConnectClient)
            Log.d(
                "NavigatorObjViewModel",
                "isPermissionsGranted: ${areAllPermissionsGranted.value}"
            )
        }
    }

    fun checkIfUserInfoLoaded() {
        _isUserInfoLoaded.value = null
        viewModelScope.launch {
            _isUserInfoLoaded.value = dbService.checkIfUserInfoLoaded()
            Log.d("NavigatorObjViewModel", "isUserInfoLoaded: ${isUserInfoLoaded.value}")
        }
    }

    fun setUserObj() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbService.getUserDataToUserObj()
            }
        }
    }

    fun isUserLoggedIn(): Boolean = accountService.isUserLoggedIn()
}
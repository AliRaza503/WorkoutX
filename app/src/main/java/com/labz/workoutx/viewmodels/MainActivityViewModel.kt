package com.labz.workoutx.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.health.connect.client.HealthConnectClient
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.models.User
import com.labz.workoutx.services.auth.AccountService
import com.labz.workoutx.services.db.DBService
import com.labz.workoutx.utils.Consts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    application: Application, // Application is injected
    private val accountService: AccountService,
    private val dbService: DBService
) : AndroidViewModel(application) {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private val _isHealthConnectSDKUnavailable = MutableStateFlow(true)
    val isHealthConnectSDKUnavailable = _isHealthConnectSDKUnavailable.asStateFlow()

    init {
        val context = application.applicationContext
        loadUserData {
            Log.d(
                "${Consts.LOG_TAG}_MainActivityViewModel",
                "loadUserData: Done ${User.email} has weight in KGs = ${User.weightInKgs}"
            )
        }
        checkAndInstallHealthConnectSDK(context = context)
    }

    fun checkAndInstallHealthConnectSDK(context: Context) {
        val availabilityStatus =
            HealthConnectClient.getSdkStatus(context = context)
        if (availabilityStatus == HealthConnectClient.SDK_AVAILABLE) {
            Log.d("${Consts.LOG_TAG}_MainActivity", "Health Connect is available")
            _isHealthConnectSDKUnavailable.value = false
        } else {
            Toast.makeText(context, "Health Connect is not available", Toast.LENGTH_SHORT).show()
            // Build the URL to allow the user to install the Health Connect package
            val url = Uri.parse("market://details")
                .buildUpon()
                .appendQueryParameter("id", "com.google.android.apps.healthdata")
                // Additional parameter to execute the onboarding flow.
                .appendQueryParameter("url", "healthconnect://onboarding")
                .build()
            Log.d("${Consts.LOG_TAG}_MainActivity", "URI: $url")
            context.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setPackage("com.android.vending")
                    data = url
                    putExtra("overlay", true)
                    putExtra("callerId", context.packageName)
                }.also { intent ->
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
            return
        }
    }

    fun loadUserData(onDataLoaded: () -> Unit) {
        viewModelScope.launch {
            Log.d("${Consts.LOG_TAG}_MainActivityViewModel", "loadUserData: ")
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                dbService.getUserDataToUserObj()
            }
            _isLoading.value = false
            onDataLoaded()
        }
    }

    fun isUserLoggedIn(): Boolean {
        return accountService.isUserLoggedIn()
    }
}
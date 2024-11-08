package com.labz.workoutx.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.services.auth.AccountService
import com.labz.workoutx.uistates.DashboardUiState
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
//    private val dbService: DBService
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()


    fun signOut(onSignOutSucceeded: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCircularProgressIndicatorVisible = true) }
            accountService.signOut { result ->
                _uiState.update { it.copy(isCircularProgressIndicatorVisible = false) }
                if (result == null) {
                    Log.d("${Consts.LOG_TAG}_DashboardViewModel", "signOut: Success")
                    onSignOutSucceeded()
                } else {
                    Log.e(
                        "${Consts.LOG_TAG}_DashboardViewModel",
                        "signOut: ${result.localizedMessage}"
                    )
                }
            }
        }
    }

    fun progressesAreLoaded() {
        viewModelScope.launch {
            _uiState.update { it.copy(areProgressesLoaded = true) }
        }
    }
}
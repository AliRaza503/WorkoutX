package com.labz.workoutx.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labz.workoutx.services.auth.AccountService
import com.labz.workoutx.services.db.DBService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NavigatorObjViewModel @Inject constructor(
    private val accountService: AccountService,
    private val dbService: DBService
) : ViewModel() {
    fun checkIfUserInfoLoaded(): Boolean {
        var exists = false
        viewModelScope.launch {
            exists = dbService.checkIfUserInfoLoaded()
        }

        return exists
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
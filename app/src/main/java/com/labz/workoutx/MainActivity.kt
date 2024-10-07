package com.labz.workoutx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.labz.workoutx.services.providers.AccountModule
import com.labz.workoutx.services.auth.AccountService
import com.labz.workoutx.ui.onboarding.OnBoardingHost
import com.labz.workoutx.ui.theme.WorkoutXTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var accountService: AccountService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        accountService = AccountModule.provideAccountService()
        setContent {
            WorkoutXTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OnBoardingHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
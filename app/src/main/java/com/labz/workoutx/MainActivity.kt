package com.labz.workoutx

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.labz.workoutx.services.auth.AccountService
import com.labz.workoutx.services.db.DBService
import com.labz.workoutx.services.healthConnect.HealthConnectService
import com.labz.workoutx.services.modules.AccountModule
import com.labz.workoutx.services.modules.DBModule
import com.labz.workoutx.services.modules.HealthConnectModule
import com.labz.workoutx.services.modules.PermissionsModule
import com.labz.workoutx.services.permissions.PermissionsService
import com.labz.workoutx.ui.dashboard.NavigatorObj
import com.labz.workoutx.ui.onboarding.OnBoardingHost
import com.labz.workoutx.ui.theme.WorkoutXTheme
import com.labz.workoutx.utils.Consts
import com.labz.workoutx.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var accountService: AccountService

    @Inject
    lateinit var dbService: DBService

    @Inject
    lateinit var healthConnectService: HealthConnectService

    @Inject
    lateinit var  permissionsService: PermissionsService

    private val viewModel: MainActivityViewModel by viewModels()

    private fun initGlobalDependencies() {
        accountService = AccountModule.provideAccountService()
        dbService = DBModule.provideDBService()
        healthConnectService = HealthConnectModule.provideHealthConnectService()
        permissionsService = PermissionsModule.providePermissionsService()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkAndInstallHealthConnectSDK(context = this.applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false) // Optional for older devices

        initGlobalDependencies()

        installSplashScreen().setKeepOnScreenCondition {
            // The viewModel is accessed and so initialized here. This makes the loading of user data to be done in the background while the splash screen is shown
            viewModel.isLoading.value == true || viewModel.isHealthConnectSDKUnavailable.value == true
        }
        Log.d(
            "${Consts.LOG_TAG}_MainActivity",
            "User loading status: ${viewModel.isLoading.value} and Health Connect is available: ${!viewModel.isHealthConnectSDKUnavailable.value}"
        )
        // Using the enable edgeToEdge() AFTER the splashScreen is a must otherwise there will be an action bar shown
        enableEdgeToEdge()
        setContent {
            WorkoutXTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
                    val isHealthConnectSDKUnavailable by viewModel.isHealthConnectSDKUnavailable.collectAsStateWithLifecycle()
                    // Go for the onboarding screens only if the user is not logged in
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading || isHealthConnectSDKUnavailable) {
                            if (isLoading) {
                                Text("Loading user data...")
                            } else {
                                Text("Health Connect app is not available! Please install it.")
                            }
                            CircularProgressIndicator()
                        } else {
                            if (!viewModel.isUserLoggedIn()) {
                                Log.e("${Consts.LOG_TAG}_MainActivity", "User is not logged in")
                                OnBoardingHost(modifier = Modifier.padding(innerPadding))
                            } else {
                                NavigatorObj.NavigatorComposable(viewModel = hiltViewModel())
                            }
                        }
                    }

                }
            }
        }
    }
}
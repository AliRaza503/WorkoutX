package com.labz.workoutx.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.labz.workoutx.ui.exts.ProgressesComposable
import com.labz.workoutx.viewmodels.DashboardViewModel
import kotlinx.serialization.Serializable

@Serializable
object DashBoardScreen {
    @Composable
    fun DashBoardScreenComposable(
        signedOut: () -> Unit,
        noPermissionsGrantedAction: () -> Unit,
        viewModel: DashboardViewModel = hiltViewModel()
    ) {
        Log.d("XYZCYZ", "DashBoardScreenComposable called")
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

//        val onPermissionsResult = { viewModel.initialLoad() }
//        val permissionsLauncher =
//            rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
//                onPermissionsResult()
//            }
//        LaunchedEffect(key1 = Unit) {
//            if(!viewModel.allPermissionsGranted()) {
//                noPermissionsGrantedAction()
//            }
//        }
//        LaunchedEffect(uiState.allPermissionsGranted) {
//            if (!uiState.allPermissionsGranted) {
//                val permissions = viewModel.getPermissionsList()
//                if (permissions.isNotEmpty()) {
//                    permissionsLauncher.launch(permissions)
//                }
//            } else {
//                viewModel.initialLoad()
//            }
//        }

        LaunchedEffect(key1 = Unit) {
            viewModel.allPermissionsGranted()
            if (!uiState.allPermissionsGranted) {
                noPermissionsGrantedAction()
            }
        }
        val isSignOutSucceeded by viewModel.isSignOutSucceeded.collectAsStateWithLifecycle()
        LaunchedEffect(isSignOutSucceeded) {
            if (isSignOutSucceeded) {
                signedOut()
                viewModel.onNavigated()
            }
        }

        when {
            uiState.isCircularProgressIndicatorVisible -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                // Your normal content when permissions are granted
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ProgressesComposable()
                }
            }
        }
    }
}
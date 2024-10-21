package com.labz.workoutx.ui.screens

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.HealthConnectClient
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.labz.workoutx.R
import com.labz.workoutx.uistates.PermissionsUiState
import com.labz.workoutx.viewmodels.PermissionsViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.serialization.Serializable

@Serializable
object PermissionsScreen {
    @Composable
    fun PermissionsScreenComposable(
        onPermissionsGranted: () -> Unit,
        viewModel: PermissionsViewModel = hiltViewModel()
    ) {
        Log.d("XYZCYZ", "PermissionsScreenComposable called")
        // Observe the UI state from the ViewModel
        val uiState by viewModel.permissionsUiState.collectAsStateWithLifecycle()

        // Permissions launcher
        val permissionsLauncher =
            rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
                Log.d("PermissionsScreen", "PermissionsLauncher result received")
                viewModel.checkPermissions()
            }

        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(lifecycleOwner) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe uiState changes using snapshotFlow
                snapshotFlow { uiState }
                    .distinctUntilChanged() // Optional: Avoid reacting to identical state changes
                    .filterNotNull()         // Optional: Filter out nulls if needed
                    .collect { state ->
                        // Perform an action based on the new state
                        if (uiState is PermissionsUiState.Uninitialized) {
                            val permissions = viewModel.getPermissionsSet()
                            if (permissions.isNotEmpty()) {
                                Log.d("PermissionsScreen", "Launching permissions request")
                                permissionsLauncher.launch(permissions)
                            } else {
                                Log.d("PermissionsScreen", "No permissions to request")
                            }
                        } else if (uiState is PermissionsUiState.Granted) {
                            onPermissionsGranted()
                        }
                    }
            }
        }

        // Launch the permissions request when the UI is in Uninitialized state
//        LaunchedEffect(uiState) {
//            if (uiState is PermissionsUiState.Uninitialized) {
//                val permissions = viewModel.getPermissionsSet()
//                if (permissions.isNotEmpty()) {
//                    Log.d("PermissionsScreen", "Launching permissions request")
//                    permissionsLauncher.launch(permissions)
//                } else {
//                    Log.d("PermissionsScreen", "No permissions to request")
//                }
//            } else if (uiState is PermissionsUiState.Granted) {
//                onPermissionsGranted()
//            }
//            while (uiState is PermissionsUiState.Uninitialized) {
//                viewModel.checkPermissions()
//                delay(1000)
//            }
//        }

        // State management
        val snackbarHostState = remember { SnackbarHostState() }

        // Handle different UI states
        when (uiState) {
            PermissionsUiState.Uninitialized -> {
                Log.d("PermissionsScreen", "Permissions not granted, showing fallback UI")
                val context = LocalContext.current

                // Show the fallback UI for manual permission setting
                GetPermissionsComposable {
                    val settingsIntent = Intent()
                    settingsIntent.action = HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS
                    context.startActivity(settingsIntent)
                }
            }

            PermissionsUiState.Granted -> {
                // Once permissions are granted, call the onPermissionsGranted callback
                Log.d("PermissionsScreen", "Permissions granted, navigating away")
                onPermissionsGranted()
            }

            is PermissionsUiState.Error -> {
                // Handle errors by showing a Snackbar with the error message
                val errorMessage = (uiState as PermissionsUiState.Error).exception.localizedMessage
                LaunchedEffect(errorMessage) {
                    errorMessage?.let {
                        snackbarHostState.showSnackbar(it)
                    }
                }
            }
        }
    }


    @Composable
    private fun GetPermissionsComposable(onRequestPermission: () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            // Guiding text with icons
            Text(
                text = "To use all features of WorkoutX, please grant permissions.",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // App Permissions Text + Icon
                Icon(
                    painter = painterResource(id = R.drawable.connectivity),
                    contentDescription = "App Permissions",
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Go to App permissions",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // WorkoutX App Text + Logo
                Icon(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "WorkoutX",
                    modifier = Modifier.size(30.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select WorkoutX",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.switch_on),
                    contentDescription = "Allow All",
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Click Allow all",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            // Button to request permission
            Button(
                onClick = onRequestPermission,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Grant Permissions")
            }
        }
    }
}


package com.gunishjain.locationviewer.screens

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.gunishjain.locationviewer.navigation.Routes
import com.gunishjain.locationviewer.viewmodels.AuthViewModel
import com.gunishjain.locationviewer.viewmodels.MapViewModel

@Composable
fun Home(navController: NavHostController) {

    val authViewModel : AuthViewModel = viewModel()
    val mapsViewModel : MapViewModel = viewModel()
    val ctx = LocalContext.current
    val firebaseUser by authViewModel.firebaseUser.collectAsState()

    val location = com.google.android.gms.maps.model.LatLng(28.613939, 77.209023)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 10f)
    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            mapsViewModel.getDeviceLocation(fusedLocationProviderClient)
        }
    }

     fun askPermissions() = when {
        ContextCompat.checkSelfPermission(
            ctx,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            mapsViewModel.getDeviceLocation(fusedLocationProviderClient)
        }
        else -> {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }


    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navController.navigate(Routes.Login.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {

        Text(text = "Hi User")

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Button(modifier = Modifier.width(100.dp),
                onClick = {
                    authViewModel.logOut()
                },
            ) {
                Text(text = "LOGOUT")
            }

            Button(modifier = Modifier.width(125.dp),
                onClick = {

                },
            ) {
                Text(text = "Share Location")
            }
        }



        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = location),
                title = "Here"

            )
        }
    }


}
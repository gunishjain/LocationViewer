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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.gunishjain.locationviewer.navigation.Routes
import com.gunishjain.locationviewer.viewmodels.AuthViewModel
import com.gunishjain.locationviewer.viewmodels.MapViewModel

@Composable
fun Home(navController: NavHostController) {

    val authViewModel: AuthViewModel = viewModel()
    val mapsViewModel: MapViewModel = viewModel()
    val ctx = LocalContext.current
    val firebaseUser by authViewModel.firebaseUser.collectAsState()
    val location by mapsViewModel.lastLocation.collectAsState()
    var shouldAnimateToUserLocation by remember { mutableStateOf(false) }

    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(ctx)
    }

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
        } else {
            askPermissions()
            shouldAnimateToUserLocation = true
        }
    }

    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(
//            location?.let {
//                LatLng(it.latitude, it.longitude)
//            } ?: LatLng(0.0, 0.0),
//            10f
//        )
    }


    LaunchedEffect(location, shouldAnimateToUserLocation) {
        if (shouldAnimateToUserLocation) {
            location?.let {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.latitude, it.longitude), 15f
                    )
                )
            }
            shouldAnimateToUserLocation = false
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {

        Text(text = "Hi User")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Button(
                modifier = Modifier.width(125.dp),
                onClick = {
                    authViewModel.logOut()
                },
            ) {
                Text(text = "LOGOUT")
            }

            Button(
                modifier = Modifier.width(125.dp),
                onClick = {
                    shouldAnimateToUserLocation = true
                },
            ) {
                Text(text = "Show Current")
            }
        }


        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            location?.let {
                Marker(
                    state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                    title = "Here"
                )

            }

        }
    }


}
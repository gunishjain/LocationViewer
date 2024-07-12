package com.gunishjain.locationviewer.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient

class MapViewModel : ViewModel() {




    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    state.value = state.value.copy(
//                        lastKnownLocation = task.result,
//                    )
                }
            }
        } catch (e: SecurityException) {
            // Show error or something
        }
    }







}
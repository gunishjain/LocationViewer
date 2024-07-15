package com.gunishjain.locationviewer.viewmodels

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {


    private val _lastLocation = MutableStateFlow<Location?>(
        null
    )
    val lastLocation: StateFlow<Location?> = _lastLocation


    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        viewModelScope.launch {
            try {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let {
                            _lastLocation.value = it
                            Log.d("MapViewModel", "Location: ${it.latitude}, ${it.longitude}")
                        }
                    } else {
                        Log.d("mapvm","lcoation fetching failed")
                    }
                }
            } catch (e: SecurityException) {
               Log.d("mapvm",e.message.toString())
            }
        }

    }


}
package com.gunishjain.locationviewer.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.gunishjain.locationviewer.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController) {
    
    
    Text(text = "Splash")


    LaunchedEffect(true) {
        delay(3000)

        if (FirebaseAuth.getInstance().currentUser!=null)
            navController.navigate(Routes.Home.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        else
            navController.navigate(Routes.Login.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }

    }

}
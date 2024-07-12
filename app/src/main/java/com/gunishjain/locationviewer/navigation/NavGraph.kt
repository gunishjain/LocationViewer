package com.gunishjain.locationviewer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gunishjain.locationviewer.screens.Home
import com.gunishjain.locationviewer.screens.Login
import com.gunishjain.locationviewer.screens.Register
import com.gunishjain.locationviewer.screens.Splash

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Routes.Splash.routes ) {

        composable(Routes.Splash.routes) {
            Splash(navController)
        }

        composable(Routes.Login.routes) {
            Login(navController)
        }

        composable(Routes.Register.routes) {
            Register(navController)
        }

        composable(Routes.Home.routes) {
            Home(navController)
        }

    }




}
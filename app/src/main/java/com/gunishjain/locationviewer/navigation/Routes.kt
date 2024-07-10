package com.gunishjain.locationviewer.navigation

sealed class Routes(val routes: String) {

    object Splash: Routes("splash")
    object Home: Routes("home")
    object Login: Routes("login")
    object Register: Routes("register")


}
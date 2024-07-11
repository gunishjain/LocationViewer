package com.gunishjain.locationviewer.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gunishjain.locationviewer.viewmodels.AuthViewModel

@Composable
fun Home() {

    val authViewModel : AuthViewModel = viewModel()

Text(text = "HOME")

    Button(onClick = {
        authViewModel.logOut()
    }) {
        Text(text ="LOGOUT")
    }

}
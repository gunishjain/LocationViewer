package com.gunishjain.locationviewer.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.gunishjain.locationviewer.navigation.Routes
import com.gunishjain.locationviewer.viewmodels.AuthViewModel

@Composable
fun Login(navController: NavHostController) {


    val ctx = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authViewModel : AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.collectAsState()
    val error by authViewModel.error.collectAsState()

    LaunchedEffect(firebaseUser) {
        if(firebaseUser!=null){
            navController.navigate(Routes.Home.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    error?.let {
        Toast.makeText(ctx,it,Toast.LENGTH_SHORT)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.width(200.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom

        ) {
            Button(onClick = {

                authViewModel.login(email,password,ctx)

            }) {
                Text(text = "Login")
            }

            Button(onClick = {

                navController.navigate(Routes.Register.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }

            }) {
                Text(text = "Register")
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun LoginView() {
//    Login()
}

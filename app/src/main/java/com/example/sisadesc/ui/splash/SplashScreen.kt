package com.example.sisadesc.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.sisadesc.core.navigation.AppScreens
import com.example.sisadesc.core.auth.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: UserViewModel,
) {
    val userData by viewModel.userData.observeAsState(initial = null)

    LaunchedEffect(key1 = true) {
        delay(1500)
        navController.popBackStack()

        val auth: FirebaseAuth = Firebase.auth

        if (auth.currentUser == null || userData == null) {
            navController.navigate(AppScreens.AuthScreen.route)
        } else navController.navigate(AppScreens.HomeScreen.route)
    }
    SplashContent()
}

@Composable
fun SplashContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cargando...")
    }
}
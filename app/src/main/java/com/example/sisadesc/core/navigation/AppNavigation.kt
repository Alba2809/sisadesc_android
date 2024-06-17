package com.example.sisadesc.core.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sisadesc.ui.auth.AuthViewModel
import com.example.sisadesc.ui.auth.LoginScreen
import com.example.sisadesc.ui.home.HomeScreen
import com.example.sisadesc.ui.home.HomeViewModel
import com.example.sisadesc.ui.navigation.NavigationHeader
import com.example.sisadesc.ui.navigation.NavigationViewModel
import com.example.sisadesc.ui.splash.SplashScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val homeViewModel = HomeViewModel()

    Scaffold(
        topBar = {
            if (currentRoute != AppScreens.SplashScreen.route && currentRoute != AppScreens.AuthScreen.route) {
                val title = when (currentRoute) {
                    AppScreens.HomeScreen.route -> "Home"
                    else -> ""
                }
                NavigationHeader(navController, homeViewModel, NavigationViewModel(), title)
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.SplashScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = 1000)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 0 },
                    animationSpec = tween(durationMillis = 1000)
                )
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreens.SplashScreen.route) {
                SplashScreen(navController)
            }
            composable(
                route = AppScreens.AuthScreen.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(durationMillis = 1000)
                    )
                }
            ) {
                LoginScreen(navController, AuthViewModel())
            }
            composable(
                route = AppScreens.HomeScreen.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(durationMillis = 1000)
                    )
                },
            ) {
                HomeScreen(navController, homeViewModel, modifier = Modifier)
            }
        }

    }

}
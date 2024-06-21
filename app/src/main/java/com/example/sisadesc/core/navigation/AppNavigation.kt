package com.example.sisadesc.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sisadesc.core.auth.UserViewModel
import com.example.sisadesc.ui.auth.AuthViewModel
import com.example.sisadesc.ui.auth.LoginScreen
import com.example.sisadesc.ui.home.HomeScreen
import com.example.sisadesc.ui.navigation.NavigationDrawerSheet
import com.example.sisadesc.ui.navigation.NavigationHeader
import com.example.sisadesc.ui.splash.SplashScreen
import com.example.sisadesc.ui.user.UserScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val auth = Firebase.auth

    val userViewModel = UserViewModel()
    val userData by userViewModel.userLoggedData.observeAsState(initial = null)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerContent = {
                NavigationDrawerSheet(
                    viewModel = userViewModel,
                    navController = navController,
                    scope = scope,
                    drawerState = drawerState,
                    currentRoute = currentRoute,
                )
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    if (currentRoute != AppScreens.SplashScreen.route && currentRoute != AppScreens.AuthScreen.route) {
                        val title = when (currentRoute) {
                            AppScreens.HomeScreen.route -> "Home"
                            AppScreens.UsersScreen.route -> "Users"
                            else -> ""
                        }
                        NavigationHeader(
                            title = title,
                            avatarUrl = userData?.avatarUrl,
                            scope = scope,
                            drawerState = drawerState
                        ) {
                            auth.signOut()
                            navController.navigate(AppScreens.AuthScreen.route)
                        }
                    }
                },
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = AppScreens.SplashScreen.route,
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it / 2 },
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
                        SplashScreen(navController, userViewModel)
                    }
                    composable(
                        route = AppScreens.AuthScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it / 2 },
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
                                initialOffsetX = { it / 2 },
                                animationSpec = tween(durationMillis = 1000)
                            )
                        },
                    ) {
                        HomeScreen(navController, userViewModel, modifier = Modifier)
                    }

                    // Admin routes
                    composable(
                        route = AppScreens.UsersScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it / 2 },
                                animationSpec = tween(durationMillis = 1000)
                            )
                        },
                    ) {
                        UserScreen(navController)
                    }
                }

            }
        }
    }


}
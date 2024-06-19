package com.example.sisadesc.core.navigation

sealed class AppScreens(val route: String) {
    data object SplashScreen: AppScreens("slash_screen")
    data object AuthScreen: AppScreens("auth_screen")
    data object HomeScreen: AppScreens("home_screen")
    data object UsersScreen: AppScreens("users_screen")
}
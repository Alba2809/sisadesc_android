package com.example.sisadesc.core.navigation

sealed class ScreensByRole(val routes: List<String>) {
    data object Admin: ScreensByRole(listOf(AppScreens.HomeScreen.route))
    data object Teacher: ScreensByRole(listOf(AppScreens.HomeScreen.route))
}

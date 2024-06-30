package com.example.sisadesc.core.navigation

sealed class AppScreens(val route: String, val title: String? = "") {
    data object SplashScreen: AppScreens("slash_screen")
    data object AuthScreen: AppScreens("auth_screen")
    data object HomeScreen: AppScreens("home_screen", "Home")
    data object UsersScreen: AppScreens("users_screen", "Usuarios")
    data object PostsScreen: AppScreens("posts_screen", "Avisos")
    data object CreatePostScreen: AppScreens("create_post_screen", "Registro de aviso")
    data object EventsScreen: AppScreens("events_screen", "Eventos")
}
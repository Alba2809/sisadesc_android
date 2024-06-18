package com.example.sisadesc.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sisadesc.core.model.NavigationItem
import com.example.sisadesc.ui.auth.AuthViewModel
import com.example.sisadesc.ui.auth.LoginScreen
import com.example.sisadesc.ui.home.HomeScreen
import com.example.sisadesc.ui.home.HomeViewModel
import com.example.sisadesc.ui.navigation.NavigationHeader
import com.example.sisadesc.ui.splash.SplashScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val auth = Firebase.auth

    val homeViewModel = HomeViewModel()
    val userData by homeViewModel.userData.observeAsState(initial = null)

    val items = listOf(
        NavigationItem(
            title = "Home",
            selectedIcon = Icons.Default.Home,
            unSelectedIcon = Icons.Default.Home,
            destination = AppScreens.HomeScreen.route
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableIntStateOf(0)
        }

        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp))
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = { Text(text = item.title) },
                            selected = index == selectedItemIndex,
                            onClick = {
                                if (currentRoute != item.destination) navController.navigate(item.destination)
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unSelectedIcon,
                                    contentDescription = "Icon de ${item.title}"
                                )
                            },
                            badge = {
                                item.badgeCount?.let {
                                    Text(text = item.badgeCount.toString())
                                }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    if (currentRoute != AppScreens.SplashScreen.route && currentRoute != AppScreens.AuthScreen.route) {
                        val title = when (currentRoute) {
                            AppScreens.HomeScreen.route -> "Home"
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
                        SplashScreen(navController, homeViewModel)
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
    }


}
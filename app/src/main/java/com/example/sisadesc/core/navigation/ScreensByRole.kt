package com.example.sisadesc.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.sisadesc.core.model.NavigationItem
import com.example.sisadesc.core.model.NavigationMenu
import java.util.UUID

sealed class ScreensByRole(val routes: List<NavigationMenu>) {
    data object Admin :
        ScreensByRole(
            listOf(
                NavigationMenu(
                    uuid = "home",
                    mainTitle = "Home",
                    mainIcon = Icons.Default.Home,
                    routes = listOf(
                        NavigationItem(
                            uuid = "home-1",
                            title = "Home",
                            selectedIcon = Icons.Default.Home,
                            unSelectedIcon = Icons.Default.Home,
                            destination = AppScreens.HomeScreen.route
                        )
                    )
                ),
                NavigationMenu(
                    uuid = "users",
                    mainTitle = "Usuarios",
                    mainIcon = Icons.Default.AccountBox,
                    routes = listOf(
                        NavigationItem(
                            uuid = "users-1",
                            title = "Lista de usuarios",
                            selectedIcon = Icons.Default.Person,
                            unSelectedIcon = Icons.Default.Person,
                            destination = AppScreens.UsersScreen.route
                        ),
                        NavigationItem(
                            uuid = "users-2",
                            title = "Registrar usuario",
                            selectedIcon = Icons.Default.Person,
                            unSelectedIcon = Icons.Default.Person,
                            destination = AppScreens.UsersScreen.route
                        )
                    )
                ),
            )
        )

    data object Teacher : ScreensByRole(
        listOf(
            NavigationMenu(
                uuid = "home",
                mainTitle = "Home",
                mainIcon = Icons.Default.Home,
                routes = listOf(
                    NavigationItem(
                        uuid = "home-1",
                        title = "Home",
                        selectedIcon = Icons.Default.Home,
                        unSelectedIcon = Icons.Default.Home,
                        destination = AppScreens.HomeScreen.route
                    )
                )
            ),
        )
    )

    data object Empty : ScreensByRole(
        listOf(
            NavigationMenu(
                uuid = "home",
                mainTitle = "Home",
                mainIcon = Icons.Default.Home,
                routes = listOf(
                    NavigationItem(
                        uuid = "home-1",
                        title = "Home",
                        selectedIcon = Icons.Default.Home,
                        unSelectedIcon = Icons.Default.Home,
                        destination = AppScreens.HomeScreen.route
                    )
                )
            ),
        )
    )
}

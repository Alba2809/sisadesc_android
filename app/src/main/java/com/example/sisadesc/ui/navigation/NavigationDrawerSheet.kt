package com.example.sisadesc.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sisadesc.core.auth.UserViewModel
import com.example.sisadesc.core.model.NavigationMenu
import com.example.sisadesc.core.navigation.ScreensByRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerSheet(
    viewModel: UserViewModel,
    navController: NavController,
    scope: CoroutineScope,
    drawerState: DrawerState,
    currentRoute: String?,
) {
    val userData by viewModel.userLoggedData.observeAsState(initial = null)

    val items: List<NavigationMenu> = when (userData?.roleData?.name) {
        "admin" -> ScreensByRole.Admin.routes
        "teacher" -> ScreensByRole.Teacher.routes
        else -> {
            ScreensByRole.Empty.routes
        }
    }

    var selectedMenuUUID by rememberSaveable {
        val homeUUID = items.first().uuid
        mutableStateOf(homeUUID)
    }

    var selectedSubmenuUUID by rememberSaveable {
        mutableStateOf("")
    }

    var expandedUUID by rememberSaveable {
        mutableStateOf("")
    }

    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        items.forEach { item ->
            val firstItem = item.routes[0]
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = if(selectedSubmenuUUID.contains(item.uuid) || item.routes.size == 1) Color(
                            0xffdbe2f9
                        ) else Color(0xFFECF0FD)
                    ),
                    label = { Text(text = item.mainTitle, color = Color.Black) },
                    selected = item.uuid == selectedMenuUUID || selectedSubmenuUUID.contains(
                        item.uuid
                    ),
                    onClick = {
                        selectedMenuUUID = item.uuid
                        if (item.routes.size == 1) {
                            scope.launch {
                                drawerState.close()
                            }

                            if (currentRoute != firstItem.destination) navController.navigate(
                                firstItem.destination
                            )
                        } else {
                            expandedUUID = item.uuid
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.mainIcon,
                            contentDescription = "Icon de ${item.mainTitle}",
                            tint = Color.DarkGray
                        )
                    },
                    badge = {
                        if (item.routes.size == 1) {
                            firstItem.badgeCount?.let {
                                Text(
                                    text = firstItem.badgeCount.toString(),
                                    color = Color.Black
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Icono de submenu",
                                tint = Color.DarkGray
                            )
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                if (item.routes.size > 1) {
                    Box(modifier = Modifier.align(Alignment.TopEnd).padding(end = 5.dp)){
                        DropdownMenu(
                            expanded = expandedUUID == item.uuid,
                            onDismissRequest = {
                                expandedUUID = ""
                                selectedMenuUUID = ""
                            },
                            modifier = Modifier.background(Color.White)
                        ) {
                            item.routes.forEach { route ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = route.title, color = Color.Black)
                                    },
                                    onClick = {
                                        selectedSubmenuUUID = route.uuid
                                        expandedUUID = ""
                                        scope.launch {
                                            drawerState.close()
                                        }
                                        if (currentRoute != route.destination) navController.navigate(
                                            route.destination
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = route.selectedIcon,
                                            contentDescription = "Icono de la ruta",
                                            tint = Color.DarkGray
                                        )
                                    },
                                    modifier = Modifier
                                        .background(if (route.uuid == selectedSubmenuUUID) Color(0xffdbe2f9) else Color.White)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.example.sisadesc.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sisadesc.core.model.NavigationMenu
import com.example.sisadesc.core.navigation.ScreensByRole
import com.example.sisadesc.core.auth.UserViewModel
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
    var showSubMenu by rememberSaveable {
        mutableStateOf(false)
    }

    AnimatedContent(
        targetState = showSubMenu,
        label = "Animated menu",
        transitionSpec = {
            if(!this.targetState) {
                slideInHorizontally(
                    animationSpec = tween(500),
                    initialOffsetX = { it }
                ) togetherWith fadeOut(
                    animationSpec = tween(500),
                    targetAlpha = 0f
                )
            }
            else {
                slideInHorizontally(
                    animationSpec = tween(500),
                    initialOffsetX = { it }
                ) togetherWith fadeOut(
                    animationSpec = tween(500),
                    targetAlpha = 0f
                )
            }
        },
        modifier = Modifier.background(DrawerDefaults.containerColor)
    ) { targetState ->
        when (targetState) {
            true -> {
                val subMenuItems = items.find {
                    it.uuid == selectedMenuUUID
                }
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp))
                    IconButton(onClick = {
                        showSubMenu = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Icon de regresar"
                        )
                    }
                    subMenuItems?.routes?.forEach { item ->
                        NavigationDrawerItem(
                            label = { Text(text = item.title) },
                            selected = item.uuid == selectedSubmenuUUID,
                            onClick = {
                                selectedSubmenuUUID = item.uuid
                                //showSubMenu = false
                                if (currentRoute != item.destination) navController.navigate(item.destination)
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.unSelectedIcon,
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
            }

            false -> {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp))
                    items.forEach { item ->
                        val firstItem = item.routes[0]
                        NavigationDrawerItem(
                            label = { Text(text = item.mainTitle) },
                            selected = item.uuid == selectedMenuUUID || selectedSubmenuUUID.contains("${item.uuid}-"),
                            onClick = {
                                selectedMenuUUID = item.uuid
                                if (item.routes.size == 1) {
                                    if (currentRoute != firstItem.destination) navController.navigate(
                                        firstItem.destination
                                    )
                                    scope.launch {
                                        drawerState.close()
                                    }
                                } else {
                                    showSubMenu = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.mainIcon,
                                    contentDescription = "Icon de ${item.mainTitle}"
                                )
                            },
                            badge = {
                                if (item.routes.size == 1) {
                                    firstItem.badgeCount?.let {
                                        Text(text = firstItem.badgeCount.toString())
                                    }
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Icono de submenu"
                                    )
                                }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        }
    }
}
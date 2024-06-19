package com.example.sisadesc.ui.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
    val userData by viewModel.userData.observeAsState(initial = null)

    val items: List<NavigationMenu> = when(userData?.roleData?.name){
        "admin" -> ScreensByRole.Admin.routes
        "teacher" -> ScreensByRole.Teacher.routes
        else -> {
            ScreensByRole.Empty.routes
        }
    }

    var selectedItemUUID by rememberSaveable {
        val homeUUID = items.first().uuid
        mutableStateOf(homeUUID)
    }
    var selectedSubmenu by rememberSaveable {
        mutableStateOf(false)
    }

    if (!selectedSubmenu) {
        ModalDrawerSheet {
            Spacer(modifier = Modifier.height(16.dp))
            items.forEach { item ->
                val firstItem = item.routes[0]
                NavigationDrawerItem(
                    label = { Text(text = item.mainTitle) },
                    selected = item.uuid == selectedItemUUID,
                    onClick = {
                        selectedItemUUID = item.uuid
                        if (item.routes.size == 1) {
                            if (currentRoute != firstItem.destination) navController.navigate(
                                firstItem.destination
                            )
                            scope.launch {
                                drawerState.close()
                            }
                        } else {
                            selectedSubmenu = true
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
    } else {
        val subMenuItems = items.find {
            it.uuid == selectedItemUUID
        }
        ModalDrawerSheet {
            Spacer(modifier = Modifier.height(16.dp))
            IconButton(onClick = {
                selectedSubmenu = false
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Icon de regresar"
                )
            }
            subMenuItems?.routes?.forEach { item ->
                NavigationDrawerItem(
                    label = { Text(text = item.title) },
                    selected = item.uuid == selectedItemUUID,
                    onClick = {
                        selectedItemUUID = item.uuid
                        selectedSubmenu = false
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
}
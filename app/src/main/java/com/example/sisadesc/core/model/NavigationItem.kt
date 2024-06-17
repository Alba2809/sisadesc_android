package com.example.sisadesc.core.model

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val destination: String,
    val badgeCount: Int? = null
)
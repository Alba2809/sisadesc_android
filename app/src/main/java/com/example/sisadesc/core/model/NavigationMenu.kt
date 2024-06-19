package com.example.sisadesc.core.model

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationMenu(
    val uuid: String,
    val mainIcon: ImageVector,
    val mainTitle: String,
    val routes: List<NavigationItem>
)
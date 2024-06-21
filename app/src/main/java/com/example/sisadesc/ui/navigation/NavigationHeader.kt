package com.example.sisadesc.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.sisadesc.ui.theme.loadingAnimation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NavigationHeader(
    title: String = "test",
    avatarUrl: String? = null,
    scope: CoroutineScope? = null,
    drawerState: DrawerState? = null,
    logout: () -> Unit? = {}
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = { scope?.launch { drawerState?.open() } }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            IconButton(onClick = { expanded = !expanded }) {
                if (avatarUrl != null) {
                    AvatarUser(avatarUrl)
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Icon user default"
                    )
                }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Perfil")
                    },
                    onClick = {
                        expanded = false
                    }
                )
                Divider()
                DropdownMenuItem(
                    text = {
                        Text(text = "Cerrar sesión")
                    },
                    onClick = {
                        expanded = false
                        logout()
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun AvatarUser(avatarUrl: String) {
    var isLoadingCompleted by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
    ) {
        SubcomposeAsyncImage(
            model = avatarUrl,
            contentDescription = "Imagen del usuario",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .background(color = Color.LightGray, shape = CircleShape)
                .loadingAnimation(isLoadingCompleted)
        ) {
            val state = painter.state
            if (state !is AsyncImagePainter.State.Loading && state !is AsyncImagePainter.State.Error) {
                isLoadingCompleted = true
                SubcomposeAsyncImageContent()
            }
        }
    }
}


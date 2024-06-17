package com.example.sisadesc.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHeader(
    title: String,
    avatarUrl: String?,
    scope: CoroutineScope,
    drawerState: DrawerState,
) {
    TopAppBar(
        title = { Text(text = title, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            IconButton(onClick = { scope.launch { drawerState.close() } }) {
                if (avatarUrl != null) {
                    AvatarUser(avatarUrl)
                }
                else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Icon user default"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun AvatarUser(avatarUrl: String) {
    Surface(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .clickable { }
    ) {
        SubcomposeAsyncImage(
            model = avatarUrl,
            contentDescription = "Imagen del usuario",
            contentScale = ContentScale.Crop,
        ) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                CircularProgressIndicator(modifier = Modifier.padding(5.dp))
            } else {
                SubcomposeAsyncImageContent()
            }
        }
    }
}

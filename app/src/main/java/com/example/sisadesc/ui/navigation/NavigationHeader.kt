package com.example.sisadesc.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.sisadesc.R
import com.example.sisadesc.ui.home.HomeViewModel

@Composable
fun NavigationHeader(
    navController: NavController, viewModel: HomeViewModel,
    navigationViewModel: NavigationViewModel, title: String
) {
    val userData by viewModel.userData.observeAsState(initial = null)
    HeaderScreen(userData?.avatarUrl, title, navigationViewModel)
}

@Composable
fun HeaderScreen(avatarUrl: String?, title: String, navigationViewModel: NavigationViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 10.dp, end = 10.dp, top = 25.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Menu()
        Text(text = title)
        AvatarUser(avatarUrl, navigationViewModel)
    }
}

@Composable
fun AvatarUser(url: String?, navigationViewModel: NavigationViewModel) {

    Surface(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .clickable { }
    ) {
        SubcomposeAsyncImage(
            model = url,
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

@Composable
fun Menu() {
    Surface(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .clickable { }
            .padding(5.dp)
    ) {
        Image(
            painterResource(id = R.drawable.icon_more),
            contentDescription = "Icono de menú",
            contentScale = ContentScale.Crop
        )
    }
}

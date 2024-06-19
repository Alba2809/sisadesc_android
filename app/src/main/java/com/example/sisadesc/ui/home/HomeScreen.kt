package com.example.sisadesc.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.sisadesc.core.auth.UserViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: UserViewModel, modifier: Modifier) {
    val userData by viewModel.userData.observeAsState(initial = null)

    Column(
        modifier = modifier
            .background(Color.White)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(text = "Home Screen")
    }
}


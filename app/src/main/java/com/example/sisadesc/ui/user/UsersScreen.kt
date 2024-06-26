package com.example.sisadesc.ui.user

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.sisadesc.core.model.UserDetailed
import com.example.sisadesc.ui.theme.PullToRefreshLazyColumn
import com.example.sisadesc.ui.theme.loadingAnimation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UserScreen(
    navController: NavController? = null,
    viewModel: UsersViewModel? = UsersViewModel()
) {
    val users by viewModel!!.users.observeAsState(initial = emptyList())
    val isLoading: Boolean by viewModel!!.loading.observeAsState(initial = false)
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(false)
    var isSheetShow by remember {
        mutableStateOf(false)
    }
    var userSelected: UserDetailed? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit) {
        viewModel?.getUsers()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {
        PullToRefreshLazyColumn(
            items = users ?: emptyList(),
            content = { user ->
                UserCard(user, onClickDetails = {
                    isSheetShow = true
                    userSelected = user
                })
            },
            isRefreshing = isLoading,
            onRefresh = {
                scope.launch {
                    viewModel?.getUsers()
                }
            }
        )
    }

    if (isSheetShow && userSelected != null) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { isSheetShow = false },
            contentColor = Color.White
        ) {
            BottomSheetContent(userSelected!!) {
                isSheetShow = false
                userSelected = null
            }
        }
    }
}

@Composable
fun UserCard(user: UserDetailed, onClickDetails: () -> Unit) {
    Surface(
        modifier = Modifier
            .widthIn(max = 250.dp)
            .padding(5.dp),
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(10.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardHeader(user.avatarUrl, onClickDetails)
            Spacer(modifier = Modifier.height(5.dp))
            CardBody(
                role = user.roleData?.displayName ?: "",
                name = user.name,
                email = user.email
            )
        }
    }
}

@Composable
fun CardHeader(imageUrl: String, onClickDetails: () -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var isLoadingCompleted by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    var showToast by remember { mutableStateOf(false) }

    if (showToast) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Falta por implementar :D", Toast.LENGTH_SHORT).show()
            showToast = false
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "Avatar del usuario",
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

        Column(modifier = Modifier.align(Alignment.TopEnd)) {
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .size(30.dp)
                    .offset(y = (-3).dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Icono de ver más",
                    tint = Color.Gray
                )

            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color.White)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Ver detalles", color = Color.Black)
                    },
                    onClick = {
                        expanded = false
                        onClickDetails()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Icono detalles",
                            tint = Color.DarkGray
                        )
                    }
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = {
                        Text(text = "Editar", color = Color.Black)
                    },
                    onClick = {
                        expanded = false
                        showToast = true
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Icono editar",
                            tint = Color.DarkGray
                        )
                    }
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Eliminar",
                            color = Color.Red
                        )
                    },
                    onClick = {
                        expanded = false
                        showToast = true
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Icono eliminar",
                            tint = Color.Red
                        )
                    }
                )
            }
        }
    }

}

@Composable
fun CardBody(role: String, name: String, email: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = role,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            maxLines = 1,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            maxLines = 1,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = email,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color(0xFF828282),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            maxLines = 1
        )
    }
}

@Composable
fun BottomSheetContent(user: UserDetailed, onHideSheetButton: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row {
            Text(
                text = "Nombre:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = user.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                text = "E-mail:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = user.email,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                text = "Nombre:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = user.roleData?.displayName ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onHideSheetButton() },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Cerrar",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}
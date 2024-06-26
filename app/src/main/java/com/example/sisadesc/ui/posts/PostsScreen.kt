package com.example.sisadesc.ui.posts

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sisadesc.core.data.Roles
import com.example.sisadesc.core.model.UserLogged
import com.example.sisadesc.ui.theme.PullToRefreshLazyColumn
import com.example.sisadesc.ui.theme.loadingAnimation
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PostsScreen(
    navController: NavController,
    viewModel: PostsViewModel? = PostsViewModel(),
    userLogged: UserLogged?
) {
    val posts by viewModel!!.posts.observeAsState(initial = emptyList())
    val isLoading: Boolean by viewModel!!.loading.observeAsState(initial = false)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel?.loadData()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {
        PullToRefreshLazyColumn(
            items = posts,
            content = { post ->
                PostCard(
                    userLogged = userLogged!!,
                    title = post.title,
                    description = post.description,
                    date = post.date,
                    onClickEdit = {
                        navController.navigate("edit/post/${post.id}")
                    },
                    onClickDelete = {

                    }
                )
            },
            isRefreshing = isLoading,
            onRefresh = {
                scope.launch {
                    viewModel?.loadData()
                }
            }
        )
    }
}

@Preview
@Composable
fun PostCard(
    userLogged: UserLogged? = null,
    title: String = "Primer aviso",
    description: String = "Descipcion del aviso",
    date: Timestamp? = Timestamp.now(),
    onClickEdit: () -> Unit = {},
    onClickDelete: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .widthIn(max = 350.dp)
            .padding(5.dp),
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(10.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardHeader(userLogged, onClickEdit, onClickDelete)
            Spacer(modifier = Modifier.height(10.dp))
            CardBody(
                title = title,
                description = description,
                date = date
            )
        }
    }
}

@Composable
fun CardHeader(userLogged: UserLogged?, onClickEdit: () -> Unit, onClickDelete: () -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Logo",
                modifier = Modifier
                    .size(50.dp)
                    .clip(
                        CircleShape
                    ),
                tint = Color.DarkGray
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 5.dp)
            ) {
                Text(text = "PUBLICACIÓN DEL COLEGIO", fontSize = 10.sp, color = Color.Black)
                Text(
                    text = "ESCUELA SECUNDARIA TÉCNICA",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "NIVEL SECUNDARIA",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color.Black
                )
            }
        }
        if (userLogged?.roleData?.name == Roles.Admin.name) {
            Column(modifier = Modifier.align(Alignment.TopEnd)) {
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier
                        .size(30.dp)
                        .offset(y = (-8).dp, x = (8).dp)
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
                        .padding(top = 0.dp, bottom = 0.dp)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Editar", color = Color.Black)
                        },
                        onClick = {
                            expanded = false
                            onClickEdit()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Icono de editar",
                                tint = Color.DarkGray
                            )
                        }
                    )
                    Divider()
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Eliminar",
                                color = Color.Red
                            )
                        },
                        onClick = {
                            expanded = false
                            onClickDelete()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Icono de eliminar",
                                tint = Color.Red
                            )
                        }
                    )
                }
            }

        }
    }

}

@Composable
fun CardBody(title: String, description: String, date: Timestamp?) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val isExpandable by remember { derivedStateOf { textLayoutResult?.didOverflowHeight ?: false } }
    val isButtonShown by remember { derivedStateOf { isExpandable || isExpanded } }

    val simpleDateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "MX"))

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            maxLines = 2,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = simpleDateFormat.format(date!!.seconds * 1000L),
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            maxLines = 1,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = description.repeat(5),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            maxLines = if (isExpanded) Integer.MAX_VALUE else 2,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult = it },
            modifier = Modifier.animateContentSize(),
            color = Color.Black
        )
    }
    if (isButtonShown) {
        IconButton(onClick = { isExpanded = !isExpanded }) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Icono de expandir",
                modifier = Modifier.size(20.dp),
                tint = Color.DarkGray
            )
        }
    } else {
        Spacer(modifier = Modifier.height(10.dp))
    }
}

package com.example.sisadesc.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.sisadesc.core.model.UserDetailed
import com.example.sisadesc.ui.theme.Directions
import com.example.sisadesc.ui.theme.bottomBorder
import com.example.sisadesc.ui.theme.customBorder

@Preview
@Composable
fun UserScreen(
    navController: NavController? = null,
    viewModel: UsersViewModel? = UsersViewModel()
) {
    val users by viewModel!!.users.observeAsState(initial = emptyList())
    val isLoading: Boolean by viewModel!!.loading.observeAsState(initial = false)

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(10.dp),
    ) {
        if (!isLoading) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                TableScreen(users = users)
            }
        } else {
            CircularProgressIndicator()
        }
    }
}

sealed class Columns(val title: String, val minWidth: Dp, val maxWidth: Dp? = minWidth) {
    object AvatarColumn : Columns("Avatar", 80.dp)
    object NameColumn : Columns("Nombre", 100.dp)
    object EmailColumn : Columns("Correo", 200.dp)
    object RoleColumn : Columns("Rol", 180.dp)
}

@Composable
fun TableScreen(users: List<UserDetailed>) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Here is the header
        item {
            Row {
                TableHeader(
                    text = "",
                    minWidth = Columns.AvatarColumn.minWidth,
                    modifier = Modifier.customBorder(
                        1.dp,
                        Color.LightGray,
                        listOf(Directions.TOP, Directions.BOTTOM)
                    )
                )
                TableHeader(
                    text = "Nombre",
                    minWidth = Columns.NameColumn.minWidth,
                    modifier = Modifier.customBorder(
                        1.dp,
                        Color.LightGray,
                        listOf(Directions.TOP, Directions.BOTTOM)
                    )
                )
                TableHeader(
                    text = "Correo",
                    minWidth = Columns.EmailColumn.minWidth,
                    modifier = Modifier.customBorder(
                        1.dp,
                        Color.LightGray,
                        listOf(Directions.TOP, Directions.BOTTOM)
                    )
                )
                TableHeader(
                    text = "Rol",
                    minWidth = Columns.RoleColumn.minWidth,
                    modifier = Modifier.customBorder(
                        1.dp,
                        Color.LightGray,
                        listOf(Directions.TOP, Directions.BOTTOM)
                    )
                )
            }
        }
        // Here are all the lines of your table.
        items(users) { user ->
            Row(
                modifier = Modifier
                    .heightIn(max = 50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TableCellImage(
                    imageUrl = user.avatarUrl,
                    minWidth = Columns.AvatarColumn.minWidth,
                    modifier = Modifier.bottomBorder(1.dp, Color.LightGray)
                )
                TableCellText(
                    text = user.name,
                    minWidth = Columns.NameColumn.minWidth,
                    modifier = Modifier.bottomBorder(1.dp, Color.LightGray)
                )
                TableCellText(
                    text = user.email,
                    minWidth = Columns.EmailColumn.minWidth,
                    modifier = Modifier.bottomBorder(1.dp, Color.LightGray)
                )
                TableCellText(
                    text = user.roleData?.displayName ?: "",
                    minWidth = Columns.RoleColumn.minWidth,
                    modifier = Modifier.bottomBorder(1.dp, Color.LightGray)
                )
            }
        }
    }
}

@Composable
fun RowScope.TableHeader(
    text: String,
    minWidth: Dp,
    maxWidth: Dp? = minWidth,
    modifier: Modifier
) {
    Text(
        text = text,
        modifier
            .widthIn(minWidth, maxWidth!!)
            .fillMaxHeight()
            .padding(8.dp),
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun RowScope.TableCellText(
    modifier: Modifier? = Modifier,
    text: String,
    minWidth: Dp,
    maxWidth: Dp? = minWidth
) {
    Box(
        modifier = modifier!!
            .fillMaxHeight(1f)
            .widthIn(minWidth, maxWidth!!),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            Modifier
                .padding(8.dp)
                .horizontalScroll(rememberScrollState()),
            maxLines = 1,
        )
    }
}

@Composable
fun RowScope.TableCellImage(
    modifier: Modifier? = Modifier,
    imageUrl: String,
    minWidth: Dp,
    maxWidth: Dp? = minWidth,
) {
    Box(
        modifier = modifier!!
            .widthIn(minWidth, maxWidth!!)
            .fillMaxHeight(1f)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
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
}
package com.example.sisadesc.ui.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sisadesc.core.model.UserLogged

@Preview
@Composable
fun CreatePostScreen(
    navController: NavController? = rememberNavController(),
    viewModel: PostsViewModel? = PostsViewModel(),
    userLogged: UserLogged? = null
) {
    val formState by viewModel!!.formState.observeAsState(FormState())
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TitleInput(formState.title, formState.isSending) {
                viewModel!!.onFormInputChange(it, formState.description)
            }
            DescriptionInput(
                modifier = Modifier.weight(1f),
                description = formState.description,
                isSending = formState.isSending
            ) {
                viewModel!!.onFormInputChange(formState.title, it)
            }
            SubmitButton(
                isEnableToSend = formState.isEnableToSend,
                isSending = formState.isSending
            ){
                viewModel!!.onSubmitCreateForm(context, navController!!)
            }
        }
    }
}

@Composable
fun TitleInput(title: String, isSending: Boolean, onValueChanged: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = title,
        onValueChange = { onValueChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(text = "Título del aviso")
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedPlaceholderColor = Color.Gray,
            focusedPlaceholderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            focusedBorderColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black
        ),
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        maxLines = 1,
        enabled = !isSending,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )
    )
}

@Composable
fun DescriptionInput(
    modifier: Modifier,
    description: String,
    isSending: Boolean,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = description,
        onValueChange = { onValueChanged(it) },
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(text = "Descripción del aviso")
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedPlaceholderColor = Color.Gray,
            focusedPlaceholderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            focusedBorderColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black
        ),
        shape = RoundedCornerShape(10.dp),
        enabled = !isSending,
    )
}

@Composable
fun SubmitButton(isEnableToSend: Boolean, isSending: Boolean, onSubmitForm: () -> Unit) {
    Button(
        onClick = { onSubmitForm() },
        modifier = Modifier
            .fillMaxWidth(),
        enabled = isEnableToSend && !isSending,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.Gray
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        if(isSending){
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp),
                strokeWidth = 3.dp
            )
        }
        else {
            Text(
                text = "Publicar",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}
package com.example.sisadesc.ui.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sisadesc.R
import com.example.sisadesc.core.navigation.AppScreens
import com.example.sisadesc.ui.theme.ContainerBorder

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun LoginScreen(
    navController: NavController = rememberNavController(),
    viewModel: AuthViewModel = AuthViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(15.dp)
        ) {
            HeaderScreen()
            LoginBodyContent(navController, viewModel, Modifier)
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun HeaderScreen() {
    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
//        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "SISADESC",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Serif,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun LoginBodyContent(
    navController: NavController,
    viewModel: AuthViewModel,
    modifier: Modifier
) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val isLoading: Boolean by viewModel.loading.observeAsState(initial = false)
    val error: Boolean by viewModel.error.observeAsState(initial = false)
    val validData: Boolean by viewModel.validData.observeAsState(initial = false)
    val showPassword: Boolean by viewModel.showPassword.observeAsState(initial = false)
    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderLogin()
        Spacer(modifier = Modifier.height(15.dp))
        EmailField(email, isLoading) { viewModel.onLoginChanged(it, password) }
        Spacer(modifier = Modifier.height(15.dp))
        PasswordField(
            password,
            isLoading,
            showPassword,
            { viewModel.onShowPasswordChanged() },
            { viewModel.onLoginChanged(email, it) }
        )
        Spacer(modifier = Modifier.height(15.dp))
        LoginButton(validData, isLoading) {
            viewModel.signInWithEmailAndPassword(email, password, context) {
                keyboardController?.hide()
                navController.navigate(AppScreens.HomeScreen.route)
            }
        }
    }
}

@Composable
fun HeaderLogin() {
    Text(
        text = "Iniciar sesión",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = "Ingresa tu correo y contraseña para ingresar",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
}

@Composable
fun EmailField(email: String, isLoading: Boolean, onTextFiledChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = email,
        onValueChange = {
            onTextFiledChange(it)
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(text = "Email")
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
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        maxLines = 1,
        enabled = !isLoading,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )
    )
}

@Composable
fun PasswordField(
    password: String,
    isLoading: Boolean,
    showPassword: Boolean,
    onShowPasswordChanged: () -> Unit,
    onTextFiledChange: (String) -> Unit
) {
    val icon = if (showPassword)
        painterResource(id = R.drawable.hide_pass)
    else
        painterResource(id = R.drawable.show_pass)

    OutlinedTextField(
        value = password,
        onValueChange = {
            onTextFiledChange(it)
        },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = {
            Text(text = "******")
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        maxLines = 1,
        enabled = !isLoading,
        trailingIcon = {
            Image(
                painter = icon,
                contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onShowPasswordChanged() }
                    .background(color = Color.Transparent)
                    .padding(5.dp),
            )
        }
    )
}

@Composable
fun LoginButton(loginEnable: Boolean, isLoading: Boolean, onLoginSelected: () -> Unit) {
    Button(

        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth(),
        enabled = loginEnable && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.Gray
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = "Acceder",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = Color.White
        )
    }
}
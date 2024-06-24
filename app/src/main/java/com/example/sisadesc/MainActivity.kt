package com.example.sisadesc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.sisadesc.core.navigation.AppNavigation
import com.example.sisadesc.ui.theme.SisadescTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SisadescTheme(
                darkTheme = false
            ) {
                AppNavigation()
            }
        }
    }
}
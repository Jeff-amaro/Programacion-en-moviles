package com.tuapp.navlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tuapp.navlab.navigation.AppNavigation
import com.tuapp.navlab.ui.theme.NavLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavLabTheme {
                AppNavigation()
            }
        }
    }
}
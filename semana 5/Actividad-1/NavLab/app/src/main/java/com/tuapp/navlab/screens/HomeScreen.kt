package com.tuapp.navlab.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tuapp.navlab.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Pantalla Inicio")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Screen.List.route) }) {
            Text("Ir a Lista")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate(Screen.Profile.route) }) {
            Text("Ir a Perfil")
        }
    }
}
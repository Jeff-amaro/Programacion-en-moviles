package com.tecsup.cafecampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.cafecampus.ui.cliente.LoginScreen
import com.tecsup.cafecampus.viewmodel.SesionViewModel

sealed class Destino(val ruta: String) {
    object Splash : Destino("splash")
    object Login : Destino("login")
    object MenuCliente : Destino("menu_cliente")
    object PanelAdmin : Destino("panel_admin")
}

@Composable
fun NavCafe(
    sesionViewModel: SesionViewModel
) {
    val navController = rememberNavController()
    val perfilActivo by sesionViewModel.perfilActivo.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Destino.Splash.ruta
    ) {
        composable(Destino.Splash.ruta) {
            LaunchedEffect(perfilActivo) {
                when (perfilActivo) {
                    "CLIENTE" -> navController.navigate(Destino.MenuCliente.ruta) {
                        popUpTo(Destino.Splash.ruta) { inclusive = true }
                    }
                    "ADMIN" -> navController.navigate(Destino.PanelAdmin.ruta) {
                        popUpTo(Destino.Splash.ruta) { inclusive = true }
                    }
                    else -> navController.navigate(Destino.Login.ruta) {
                        popUpTo(Destino.Splash.ruta) { inclusive = true }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        composable(Destino.Login.ruta) {
            LoginScreen(
                viewModel = sesionViewModel,
                onLoginExitoso = { rol ->
                    val destino = if (rol == "ADMIN") Destino.PanelAdmin.ruta else Destino.MenuCliente.ruta
                    navController.navigate(destino) {
                        popUpTo(Destino.Login.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Destino.MenuCliente.ruta) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Menú Principal (Cliente)")
            }
        }

        composable(Destino.PanelAdmin.ruta) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Panel de Administración (Admin)")
            }
        }
    }
}
package com.tecsup.productos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.productos.domain.model.Rol
import com.tecsup.productos.ui.admin.AdminScreen
import com.tecsup.productos.ui.carrito.CarritoScreen
import com.tecsup.productos.ui.detalle.DetalleScreen
import com.tecsup.productos.ui.favoritos.FavoritosScreen
import com.tecsup.productos.ui.lista.ListaScreen
import com.tecsup.productos.ui.login.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.Login.ruta) {

        composable(Rutas.Login.ruta) {
            LoginScreen(
                onLoginExito = { rol ->
                    if (rol == Rol.ADMIN) {
                        navController.navigate(Rutas.Admin.ruta) {
                            popUpTo(Rutas.Login.ruta) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Rutas.Lista.ruta) {
                            popUpTo(Rutas.Login.ruta) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Rutas.Admin.ruta) {
            AdminScreen(
                onVerCatalogo = { navController.navigate(Rutas.Lista.ruta) },
                onCerrarSesion = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Rutas.Lista.ruta) {
            ListaScreen(
                onProductoClick = { id ->
                    navController.navigate(Rutas.Detalle.crearRuta(id))
                },
                onVerFavoritos = {
                    navController.navigate("favoritos")
                },
                onVerCarrito = {
                    navController.navigate(Rutas.Carrito.ruta)
                },
                onCerrarSesion = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable("favoritos") {
            FavoritosScreen(
                onVolver = { navController.popBackStack() },
                onProductoClick = { id ->
                    navController.navigate(Rutas.Detalle.crearRuta(id))
                }
            )
        }

        composable(Rutas.Carrito.ruta) {
            CarritoScreen(
                onVolver = { navController.popBackStack() },
                onCompraExitosa = {
                    navController.navigate(Rutas.Lista.ruta) {
                        popUpTo(Rutas.Lista.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Rutas.Detalle.ruta,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            DetalleScreen(
                idProducto = id,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
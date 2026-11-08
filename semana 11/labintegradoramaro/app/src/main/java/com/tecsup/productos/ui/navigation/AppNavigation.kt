package com.tecsup.productos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.tecsup.productos.domain.model.Rol
import com.tecsup.productos.ui.admin.AdminScreen
import com.tecsup.productos.ui.carrito.CarritoScreen
import com.tecsup.productos.ui.detalle.DetalleScreen
import com.tecsup.productos.ui.favoritos.FavoritosScreen
import com.tecsup.productos.ui.historial.HistorialScreen
import com.tecsup.productos.ui.lista.ListaScreen
import com.tecsup.productos.ui.login.LoginScreen
import com.tecsup.productos.ui.perfil.PerfilScreen

@Composable
fun AppNavigation(
    modoOscuro: Boolean = false,
    onCambiarTema: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()
    val emailActual = FirebaseAuth.getInstance().currentUser?.email ?: ""

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
                onVerPerfil = { navController.navigate("perfil_admin") },
                onCerrarSesion = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable("perfil_admin") {
            PerfilScreen(
                emailUsuario = emailActual,
                esAdmin = true,
                modoOscuro = modoOscuro,
                onCambiarTema = onCambiarTema,
                onVolver = { navController.popBackStack() }
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
                onVerHistorial = {
                    navController.navigate("historial")
                },
                onVerPerfil = {
                    navController.navigate("perfil_user")
                },
                onCerrarSesion = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable("perfil_user") {
            PerfilScreen(
                emailUsuario = emailActual,
                esAdmin = false,
                modoOscuro = modoOscuro,
                onCambiarTema = onCambiarTema,
                onVolver = { navController.popBackStack() }
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

        composable("historial") {
            HistorialScreen(
                onVolver = { navController.popBackStack() },
                onProductoClick = { id ->
                    navController.navigate(Rutas.Detalle.crearRuta(id))
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
package com.tecsup.productos.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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

sealed class BottomNavItem(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
) {
    object Catalogo : BottomNavItem(Rutas.Lista.ruta, "Catálogo", Icons.Default.Home)
    object Favoritos : BottomNavItem("favoritos", "Favoritos", Icons.Default.Favorite)
    object Carrito : BottomNavItem(Rutas.Carrito.ruta, "Carrito", Icons.Default.ShoppingCart)
    object Historial : BottomNavItem("historial", "Historial", Icons.Default.History)
    object Perfil : BottomNavItem("perfil_user", "Perfil", Icons.Default.Person)
}

@Composable
fun AppNavigation(
    modoOscuro: Boolean = false,
    onCambiarTema: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()
    val emailActual = FirebaseAuth.getInstance().currentUser?.email ?: ""

    val itemsBottomBar = listOf(
        BottomNavItem.Catalogo,
        BottomNavItem.Favoritos,
        BottomNavItem.Carrito,
        BottomNavItem.Historial,
        BottomNavItem.Perfil
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    // La barra inferior solo se muestra en las pantallas principales del usuario
    val mostrarBottomBar = itemsBottomBar.any { it.ruta == rutaActual }

    Scaffold(
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar {
                    itemsBottomBar.forEach { item ->
                        val seleccionado = rutaActual == item.ruta
                        NavigationBarItem(
                            icon = { Icon(item.icono, contentDescription = item.titulo) },
                            label = { Text(item.titulo) },
                            selected = seleccionado,
                            onClick = {
                                if (!seleccionado) {
                                    navController.navigate(item.ruta) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Rutas.Login.ruta,
            modifier = Modifier.padding(paddingValues)
        ) {

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
                    onVolver = { navController.popBackStack() },
                    onCerrarSesion = {
                        FirebaseAuth.getInstance().signOut()
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
                    onVerFavoritos = { navController.navigate("favoritos") },
                    onVerCarrito = { navController.navigate(Rutas.Carrito.ruta) },
                    onVerHistorial = { navController.navigate("historial") },
                    onVerPerfil = { navController.navigate("perfil_user") },
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
                    onVolver = { navController.popBackStack() },
                    onCerrarSesion = {
                        FirebaseAuth.getInstance().signOut()
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
}
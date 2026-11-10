package com.tecsup.productos.ui.navegacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Catalogo : BottomNavItem("lista", "Catálogo", Icons.Default.Home)
    object Favoritos : BottomNavItem("favoritos", "Favoritos", Icons.Default.Favorite)
    object Carrito : BottomNavItem("carrito", "Carrito", Icons.Default.ShoppingCart)
    object Historial : BottomNavItem("historial", "Historial", Icons.Default.History)
    object Perfil : BottomNavItem("perfil", "Perfil", Icons.Default.Person)
}
package com.amaro.lab04carritotecsup.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    opcionSeleccionada: String,
    onOpcionSeleccionada: (String) -> Unit
) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(12.dp))
        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = opcionSeleccionada == "Inicio",
            onClick = { onOpcionSeleccionada("Inicio") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            selected = opcionSeleccionada == "Mis pedidos",
            onClick = { onOpcionSeleccionada("Mis pedidos") },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Mis pedidos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Favoritos") },
            selected = opcionSeleccionada == "Favoritos",
            onClick = { onOpcionSeleccionada("Favoritos") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = opcionSeleccionada == "Perfil",
            onClick = { onOpcionSeleccionada("Perfil") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
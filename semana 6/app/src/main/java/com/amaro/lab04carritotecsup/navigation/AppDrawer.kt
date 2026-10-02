package com.amaro.lab04carritotecsup.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    pantallaActual: String,
    onNavegar: (String) -> Unit,
    onCerrarDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = "TECSUP App",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        NavigationDrawerItem(
            label = { Text("Carrito de Compras") },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") },
            selected = pantallaActual == "carrito",
            onClick = {
                onNavegar("carrito")
                onCerrarDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Mis Favoritos") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
            selected = pantallaActual == "favoritos",
            onClick = {
                onNavegar("favoritos")
                onCerrarDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
package com.amaro.lab04carritotecsup.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    opcionSeleccionada: String,
    onOpcionSeleccionada: (String) -> Unit
) {
    ModalDrawerSheet {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(24.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Usuario Tecsup",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "usuario@tecsup.edu.pe",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }

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
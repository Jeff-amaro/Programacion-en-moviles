package com.amaro.lab04carritotecsup.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amaro.lab04carritotecsup.data.Producto

@Composable
fun PantallaFavoritos(
    productosFavoritos: List<Producto>,
    onEliminar: (Producto) -> Unit,
    onToggleFavorito: (Producto) -> Unit,
    onEditarProducto: (Producto) -> Unit,
    modifier: Modifier = Modifier
) {
    if (productosFavoritos.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Aún no tienes productos favoritos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(productosFavoritos, key = { it.id }) { producto ->
                TarjetaProducto(
                    producto = producto,
                    onEliminar = { onEliminar(producto) },
                    onToggleFavorito = { onToggleFavorito(producto) },
                    onEditarProducto = { productoEditado -> onEditarProducto(productoEditado) }
                )
            }
        }
    }
}
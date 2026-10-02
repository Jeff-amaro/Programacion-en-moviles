package com.amaro.lab04carritotecsup.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amaro.lab04carritotecsup.data.Producto

@Composable
fun TarjetaProducto(
    producto: Producto,
    onEliminar: () -> Unit,
    onToggleFavorito: () -> Unit = {},
    onEditarProducto: (Producto) -> Unit = {}
) {
    val subtotalItem = producto.precio * producto.cantidad
    var mostrarMenu by remember { mutableStateOf(false) }
    var mostrarDialogoEditar by remember { mutableStateOf(false) }

    if (mostrarDialogoEditar) {
        DialogoEditarProducto(
            producto = producto,
            onConfirmar = { productoEditado ->
                onEditarProducto(productoEditado)
                mostrarDialogoEditar = false
            },
            onDismiss = { mostrarDialogoEditar = false }
        )
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "S/ ${"%.2f".format(producto.precio)} x ${producto.cantidad}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "S/ ${"%.2f".format(subtotalItem)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 4.dp)
            )

            Box {
                IconButton(onClick = { mostrarMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones"
                    )
                }

                DropdownMenu(
                    expanded = mostrarMenu,
                    onDismissRequest = { mostrarMenu = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(if (producto.esFavorito) "Quitar de Favoritos" else "Marcar como Favorito")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (producto.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {
                            mostrarMenu = false
                            onToggleFavorito()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Editar") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar"
                            )
                        },
                        onClick = {
                            mostrarMenu = false
                            mostrarDialogoEditar = true
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            mostrarMenu = false
                            onEliminar()
                        }
                    )
                }
            }
        }
    }
}
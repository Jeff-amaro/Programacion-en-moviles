package com.amaro.lab04carritotecsup.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amaro.lab04carritotecsup.data.Producto

@Composable
fun PantallaCarrito(modifier: Modifier = Modifier) {
    val listaProductos = remember { mutableStateListOf<Producto>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Mi Carrito TECSUP",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        FormularioProducto(
            onAgregarProducto = { nombre, precio, cantidad ->
                listaProductos.add(Producto(nombre, precio, cantidad))
            }
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaProductos) { producto ->
                TarjetaProducto(
                    producto = producto,
                    onEliminar = { listaProductos.remove(producto) }
                )
            }
        }

        ResumenCarrito(productos = listaProductos)
    }
}
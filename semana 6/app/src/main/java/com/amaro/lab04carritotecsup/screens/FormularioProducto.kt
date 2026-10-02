package com.amaro.lab04carritotecsup.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormularioProducto(
    onAgregarProducto: (String, Double, Int) -> Unit
) {
    var nombreInput by remember { mutableStateOf("") }
    var precioInput by remember { mutableStateOf("") }
    var cantidadInput by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = nombreInput,
            onValueChange = { nombreInput = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = precioInput,
                onValueChange = { precioInput = it },
                label = { Text("Precio (S/)") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = cantidadInput,
                onValueChange = { cantidadInput = it },
                label = { Text("Cantidad") },
                modifier = Modifier.weight(1f)
            )
        }
        Button(
            onClick = {
                val precio = precioInput.toDoubleOrNull() ?: 0.0
                val cantidad = cantidadInput.toIntOrNull() ?: 1
                if (nombreInput.isNotBlank() && precio > 0) {
                    onAgregarProducto(nombreInput, precio, cantidad)
                    nombreInput = ""
                    precioInput = ""
                    cantidadInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AGREGAR")
        }
    }
}
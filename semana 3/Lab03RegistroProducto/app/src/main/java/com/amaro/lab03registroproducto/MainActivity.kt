package com.amaro.lab03registroproducto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.amaro.lab03registroproducto.ui.theme.Lab03RegistroProductoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03RegistroProductoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaRegistro(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaRegistro(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var mostrarResumen by remember { mutableStateOf(false) }
    var textoError by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Nuevo producto",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Completa los datos y presiona Agregar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                if (textoError.isNotEmpty()) textoError = ""
            },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = precio,
                onValueChange = {
                    precio = it
                    if (textoError.isNotEmpty()) textoError = ""
                },
                label = { Text("Precio (S/)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                value = cantidad,
                onValueChange = {
                    cantidad = it
                    if (textoError.isNotEmpty()) textoError = ""
                },
                label = { Text("Cantidad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    val p = precio.toDoubleOrNull()
                    val c = cantidad.toIntOrNull()

                    when {
                        nombre.isBlank() || precio.isBlank() || cantidad.isBlank() -> {
                            textoError = "Por favor complete todos los campos"
                            mostrarResumen = false
                        }
                        p == null || p <= 0 -> {
                            textoError = "Ingrese un precio numérico válido mayor a 0"
                            mostrarResumen = false
                        }
                        c == null || c <= 0 -> {
                            textoError = "Ingrese una cantidad entera válida mayor a 0"
                            mostrarResumen = false
                        }
                        else -> {
                            textoError = ""
                            mostrarResumen = true
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("AGREGAR PRODUCTO")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = {
                    nombre = ""
                    precio = ""
                    cantidad = ""
                    mostrarResumen = false
                    textoError = ""
                }
            ) {
                Text("Limpiar")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (textoError.isNotEmpty()) {
            Text(
                text = textoError,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (mostrarResumen) {
            val precioNum = precio.toDoubleOrNull() ?: 0.0
            val cantidadNum = cantidad.toIntOrNull() ?: 0
            val importe = precioNum * cantidadNum

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(nombre, style = MaterialTheme.typography.titleLarge)
                    Text("Precio: S/ " + String.format("%.2f", precioNum))
                    Text("Cantidad: $cantidadNum")
                    Text(
                        text = "Importe: S/ " + String.format("%.2f", importe),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "✓ Producto registrado correctamente",
                color = Color(0xFF2E7D32),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
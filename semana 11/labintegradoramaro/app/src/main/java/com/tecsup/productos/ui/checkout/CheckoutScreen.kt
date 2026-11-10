package com.tecsup.productos.ui.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.productos.ui.carrito.CarritoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

enum class MetodoPago { TARJETA, YAPE, CONTRAENTREGA }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    carritoViewModel: CarritoViewModel = viewModel(factory = CarritoViewModel.Factory),
    onVolver: () -> Unit,
    onPagoExitoso: () -> Unit
) {
    val total by carritoViewModel.totalCarrito.collectAsState(initial = 0.0)

    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf(MetodoPago.TARJETA) }

    var numeroTarjeta by remember { mutableStateOf("") }
    var fechaExp by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    var numeroYape by remember { mutableStateOf("") }
    var codigoAprobacion by remember { mutableStateOf("") }

    var procesando by remember { mutableStateOf(false) }
    var mostrarExitoDialog by remember { mutableStateOf(false) }

    if (mostrarExitoDialog) {
        AlertDialog(
            onDismissRequest = {},
            icon = {
                Icon(
                    Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("¡Pago Exitoso!", fontWeight = FontWeight.Bold) },
            text = { Text("Tu pedido ha sido procesado correctamente. ¡Gracias por tu compra!") },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarExitoDialog = false
                        carritoViewModel.realizarCheckout {
                            onPagoExitoso()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al Catálogo")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Pago", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total a Pagar:", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "$${String.format("%.2f", total ?: 0.0)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text("1. Dirección de Entrega", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección de Envío") },
                leadingIcon = { Icon(Icons.Rounded.LocationOn, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono de Contacto") },
                leadingIcon = { Icon(Icons.Rounded.Phone, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Text("2. Método de Pago", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = metodoPago == MetodoPago.TARJETA,
                    onClick = { metodoPago = MetodoPago.TARJETA },
                    label = { Text("Tarjeta") },
                    leadingIcon = { Icon(Icons.Rounded.CreditCard, contentDescription = null) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = metodoPago == MetodoPago.YAPE,
                    onClick = { metodoPago = MetodoPago.YAPE },
                    label = { Text("Yape / PLIN") },
                    leadingIcon = { Icon(Icons.Rounded.QrCode, contentDescription = null) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = metodoPago == MetodoPago.CONTRAENTREGA,
                    onClick = { metodoPago = MetodoPago.CONTRAENTREGA },
                    label = { Text("Efectivo") },
                    leadingIcon = { Icon(Icons.Rounded.Payments, contentDescription = null) },
                    modifier = Modifier.weight(1f)
                )
            }

            AnimatedVisibility(visible = metodoPago == MetodoPago.TARJETA) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = numeroTarjeta,
                            onValueChange = { if (it.length <= 16) numeroTarjeta = it },
                            label = { Text("Número de Tarjeta") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = fechaExp,
                                onValueChange = { if (it.length <= 5) fechaExp = it },
                                label = { Text("MM/AA") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = cvv,
                                onValueChange = { if (it.length <= 4) cvv = it },
                                label = { Text("CVV") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = metodoPago == MetodoPago.YAPE) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Yapea al número: 987 654 321", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        OutlinedTextField(
                            value = numeroYape,
                            onValueChange = { numeroYape = it },
                            label = { Text("Número que Yapeó") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = codigoAprobacion,
                            onValueChange = { codigoAprobacion = it },
                            label = { Text("Código de Aprobación") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }

            AnimatedVisibility(visible = metodoPago == MetodoPago.CONTRAENTREGA) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Pagarás en efectivo al momento de recibir tus productos en tu domicilio.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    procesando = true
                    mostrarExitoDialog = true
                },
                enabled = direccion.isNotBlank() && telefono.isNotBlank() && !procesando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (procesando) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Rounded.Lock, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirmar y Pagar $${String.format("%.2f", total ?: 0.0)}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
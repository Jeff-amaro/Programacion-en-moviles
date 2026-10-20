package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoAdminScreen(
    pedidosAdminViewModel: PedidosAdminViewModel,
    onVolver: () -> Unit
) {
    val itemConDetalle by pedidosAdminViewModel.pedidoDetalle.collectAsState()
    val colorPurpura = Color(0xFF4F46E5)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Detalle del pedido",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF1F2937)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFFAFAFA))
            )
        },
        containerColor = Color(0xFFFAFAFA)
    ) { innerPadding ->
        itemConDetalle?.let { item ->
            val pedido = item.pedido
            val cliente = item.cliente
            val detalles = item.detalles

            val rolTexto = when (cliente.rol.uppercase()) {
                "CLIENTE", "ESTUDIANTE" -> "Estudiante"
                "ADMIN" -> "Administrador"
                else -> cliente.rol
            }

            val nombreCliente = cliente.nombre.ifBlank { "Juan Pérez" }
            val clienteNombreRol = "$nombreCliente · $rolTexto"

            val estadosDisponibles = listOf(
                "PENDIENTE" to "Recibido",
                "EN_PREPARACION" to "Preparando",
                "LISTO" to "Listo",
                "ENTREGADO" to "Entregado"
            )

            val textoBotonAvanzar = when (pedido.estado) {
                "PENDIENTE" -> "Pasar a Preparando"
                "EN_PREPARACION" -> "Pasar a Listo"
                "LISTO" -> "Pasar a Entregado"
                else -> "Avanzar estado"
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Pedido #%04d".format(pedido.id),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F2937)
                                    )
                                    Surface(
                                        color = when (pedido.estado) {
                                            "PENDIENTE" -> Color(0xFFE0E7FF)
                                            "EN_PREPARACION" -> Color(0xFFFEF3C7)
                                            "LISTO" -> Color(0xFFDCFCE7)
                                            else -> Color(0xFFF3F4F6)
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = when (pedido.estado) {
                                                "PENDIENTE" -> "Recibido"
                                                "EN_PREPARACION" -> "Preparando"
                                                "LISTO" -> "Listo"
                                                "ENTREGADO" -> "Entregado"
                                                else -> pedido.estado
                                            },
                                            color = when (pedido.estado) {
                                                "PENDIENTE" -> Color(0xFF2563EB)
                                                "EN_PREPARACION" -> Color(0xFFD97706)
                                                "LISTO" -> Color(0xFF16A34A)
                                                else -> Color.Gray
                                            },
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Hoy 10:12 · Recojo ${pedido.horaRecojo.ifBlank { "10:30" }}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Column {
                                    Text("Cliente", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = clienteNombreRol,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F2937)
                                    )
                                }

                                HorizontalDivider(color = Color(0xFFF3F4F6))

                                detalles.forEach { det ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = det.producto.nombre,
                                            fontSize = 14.sp,
                                            color = Color(0xFF1F2937),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Box(
                                            modifier = Modifier.weight(0.5f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "x${det.detalle.cantidad}",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF6B7280)
                                            )
                                        }
                                        Text(
                                            text = "S/ %.2f".format(det.producto.precio * det.detalle.cantidad),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1F2937),
                                            textAlign = TextAlign.End
                                        )
                                    }
                                }

                                HorizontalDivider(color = Color(0xFFF3F4F6))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Total", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                    Text("S/ %.2f".format(pedido.total), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colorPurpura)
                                }
                            }
                        }
                    }

                    val notaTexto = pedido.nota.ifBlank { "Sin ají, por favor" }
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                            border = BorderStroke(0.dp, Color.Transparent)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("Nota del cliente", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(notaTexto, fontSize = 14.sp, color = Color(0xFFB45309))
                            }
                        }
                    }

                    item {
                        Text("Cambiar estado", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            estadosDisponibles.forEach { (clave, label) ->
                                val seleccionada = pedido.estado == clave
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            pedidosAdminViewModel.cambiarEstadoManual(item, clave)
                                        },
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (seleccionada) Color(0xFF2563EB) else Color.White,
                                    border = BorderStroke(1.dp, if (seleccionada) Color(0xFF2563EB) else Color(0xFFE5E7EB))
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            color = if (seleccionada) Color.White else Color(0xFF6B7280),
                                            fontSize = 12.sp,
                                            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (pedido.estado != "ENTREGADO" && pedido.estado != "CANCELADO") {
                        Button(
                            onClick = {
                                pedidosAdminViewModel.avanzar(item)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorPurpura),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(textoBotonAvanzar, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (pedido.estado != "CANCELADO") {
                        OutlinedButton(
                            onClick = {
                                pedidosAdminViewModel.cancelar(item)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            border = BorderStroke(1.dp, Color(0xFFDC2626)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Cancelar pedido", color = Color(0xFFDC2626), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando detalle del pedido...", color = Color.Gray)
        }
    }
}
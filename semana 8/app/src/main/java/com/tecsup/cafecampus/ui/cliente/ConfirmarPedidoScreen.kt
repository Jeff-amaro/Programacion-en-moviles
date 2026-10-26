package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarPedidoScreen(
    menuViewModel: MenuViewModel,
    pedidosViewModel: PedidosClienteViewModel,
    sesionViewModel: SesionViewModel,
    onPedidoConfirmado: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val carrito by menuViewModel.carrito.collectAsState()
    val recibirPedidos by sesionViewModel.recibirPedidos.collectAsState()
    var horaSeleccionada by remember { mutableStateOf("10:30") }
    var formaPagoSeleccionada by remember { mutableStateOf("Pagar en caja") }

    val colorTema = Color(0xFFE65100)
    val subtotal = menuViewModel.total()
    val servicio = 0.00
    val total = subtotal + servicio

    val horasRecojo = listOf("Ahora", "10:30", "11:00", "11:30")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Confirmar pedido",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
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
                    Spacer(modifier = Modifier.height(8.dp))

                    if (!recibirPedidos) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFEF4444))
                        ) {
                            Text(
                                text = "☕ La cafetería está cerrada en este momento. No es posible enviar nuevos pedidos.",
                                color = Color(0xFF991B1B),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

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
                            carrito.forEachIndexed { index, item ->
                                val fondoBox = when {
                                    item.producto.nombre.lowercase().contains("café") -> Color(0xFFEFEBE9)
                                    item.producto.nombre.lowercase().contains("sandwich") -> Color(0xFFFFF8E1)
                                    else -> Color(0xFFFFF3E0)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .background(fondoBox, RoundedCornerShape(12.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            ProductoGrafico(
                                                nombre = item.producto.nombre,
                                                modifier = Modifier.size(36.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = item.producto.nombre,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1F2937)
                                            )
                                            Text(
                                                text = "x${item.cantidad}",
                                                fontSize = 12.sp,
                                                color = Color(0xFF9CA3AF)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "S/ %.2f".format(item.producto.precio * item.cantidad),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F2937)
                                    )
                                }

                                if (index < carrito.size - 1) {
                                    HorizontalDivider(color = Color(0xFFF3F4F6))
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Hora de recojo",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        horasRecojo.forEach { hora ->
                            val seleccionada = hora == horaSeleccionada
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { horaSeleccionada = hora },
                                shape = RoundedCornerShape(20.dp),
                                color = if (seleccionada) colorTema else Color.White,
                                border = BorderStroke(
                                    1.dp,
                                    if (seleccionada) colorTema else Color(0xFFE5E7EB)
                                )
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = hora,
                                        color = if (seleccionada) Color.White else Color(0xFF6B7280),
                                        fontSize = 13.sp,
                                        fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Forma de pago",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf("Pagar en caja", "Yape / Plin").forEach { forma ->
                            val seleccionada = forma == formaPagoSeleccionada
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { formaPagoSeleccionada = forma },
                                shape = RoundedCornerShape(16.dp),
                                color = if (seleccionada) Color(0xFFFFF3E0) else Color.White,
                                border = BorderStroke(
                                    1.dp,
                                    if (seleccionada) colorTema else Color(0xFFE5E7EB)
                                )
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = forma,
                                        color = if (seleccionada) colorTema else Color(0xFF6B7280),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
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
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Subtotal", fontSize = 13.sp, color = Color(0xFF6B7280))
                                Text("S/ %.2f".format(subtotal), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Servicio", fontSize = 13.sp, color = Color(0xFF6B7280))
                                Text("S/ %.2f".format(servicio), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                            }
                            HorizontalDivider(color = Color(0xFFE5E7EB))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                Text("S/ %.2f".format(total), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colorTema)
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        pedidosViewModel.confirmar(
                            items = carrito,
                            horaRecojo = horaSeleccionada,
                            pago = formaPagoSeleccionada,
                            nota = ""
                        ) { idNuevo ->
                            menuViewModel.vaciar()
                            onPedidoConfirmado(idNuevo)
                        }
                    },
                    enabled = recibirPedidos,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorTema,
                        disabledContainerColor = Color(0xFFD1D5DB)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (recibirPedidos) "Confirmar pedido" else "Cafetería Cerrada",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    border = BorderStroke(1.dp, colorTema),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Seguir pidiendo",
                        color = colorTema,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
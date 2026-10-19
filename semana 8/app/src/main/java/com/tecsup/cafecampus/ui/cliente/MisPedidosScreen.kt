package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPedidosScreen(
    pedidosViewModel: PedidosClienteViewModel,
    onPedidoClick: (Int) -> Unit,
    onIrAMenu: () -> Unit,
    onVolver: () -> Unit
) {
    val misPedidos by pedidosViewModel.misPedidos.collectAsState()
    var pestañaSeleccionada by remember { mutableIntStateOf(0) }

    val colorTema = Color(0xFFE65100)

    val pedidosActivos = misPedidos.filter {
        it.pedido.estado == "PENDIENTE" ||
                it.pedido.estado == "EN_PREPARACION" ||
                it.pedido.estado == "LISTO"
    }

    val pedidosHistorial = misPedidos.filter {
        it.pedido.estado == "ENTREGADO" ||
                it.pedido.estado == "CANCELADO"
    }

    val listaMostrar = if (pestañaSeleccionada == 0) pedidosActivos else pedidosHistorial

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mis pedidos",
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
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onIrAMenu,
                    icon = { Icon(Icons.Default.Home, contentDescription = "Menú", tint = Color.Gray) },
                    label = { Text("Menú", color = Color.Gray) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Menu, contentDescription = "Pedidos", tint = colorTema) },
                    label = { Text("Pedidos", color = colorTema, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.Gray) },
                    label = { Text("Perfil", color = Color.Gray) }
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF4EFEA), RoundedCornerShape(16.dp))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (pestañaSeleccionada == 0) Color.White else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { pestañaSeleccionada = 0 }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Activos",
                        color = if (pestañaSeleccionada == 0) colorTema else Color(0xFF6B7280),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (pestañaSeleccionada == 1) Color.White else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { pestañaSeleccionada = 1 }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Historial",
                        color = if (pestañaSeleccionada == 1) colorTema else Color(0xFF6B7280),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (listaMostrar.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (pestañaSeleccionada == 0) "No tienes pedidos activos" else "No tienes historial de pedidos",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumnPedidos(
                    pedidos = listaMostrar,
                    onPedidoClick = onPedidoClick,
                    colorTema = colorTema
                )
            }
        }
    }
}

@Composable
private fun LazyColumnPedidos(
    pedidos: List<PedidoConDetalle>,
    onPedidoClick: (Int) -> Unit,
    colorTema: Color
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(pedidos) { item ->
            val pedido = item.pedido
            val detalles = item.detalles

            val resumenTexto = if (detalles.isNotEmpty()) {
                val primero = "${detalles.first().producto.nombre} x${detalles.first().detalle.cantidad}"
                if (detalles.size > 1) "$primero + ${detalles.size - 1} más" else primero
            } else "Pedido sin items"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPedidoClick(pedido.id) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Ver detalle",
                            tint = Color(0xFF9CA3AF)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = resumenTexto,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Hoy 10:30",
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EstadoChip(estado = pedido.estado)

                        Text(
                            text = "S/ %.2f".format(pedido.total),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorTema
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EstadoChip(estado: String) {
    val (fondo, textoColor, label) = when (estado) {
        "PENDIENTE" -> Triple(Color(0xFFFFF3E0), Color(0xFFE65100), "Recibido")
        "EN_PREPARACION" -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), "Preparando")
        "LISTO" -> Triple(Color(0xFFDCFCE7), Color(0xFF16A34A), "Listo")
        "ENTREGADO" -> Triple(Color(0xFFF3F4F6), Color(0xFF6B7280), "Entregado")
        "CANCELADO" -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), "Cancelado")
        else -> Triple(Color(0xFFFFF3E0), Color(0xFFE65100), "Recibido")
    }

    Surface(
        color = fondo,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = label,
            color = textoColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
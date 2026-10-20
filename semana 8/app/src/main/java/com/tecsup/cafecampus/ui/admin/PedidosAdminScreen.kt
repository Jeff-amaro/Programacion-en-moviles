package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosAdminScreen(
    pedidosAdminViewModel: PedidosAdminViewModel,
    onPedidoClick: (Int) -> Unit,
    onIrAPanel: () -> Unit,
    onIrAMenu: () -> Unit,
    onIrAAjustes: () -> Unit,
    onVolver: () -> Unit
) {
    val estadoActual by pedidosAdminViewModel.estadoSeleccionado.collectAsState()
    val listaPedidos by pedidosAdminViewModel.pedidos.collectAsState()

    val colorPurpura = Color(0xFF4F46E5)

    val opcionesFiltro = listOf(
        "PENDIENTE" to "Nuevos",
        "EN_PREPARACION" to "Preparando",
        "LISTO" to "Listos"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pedidos",
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
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onIrAPanel,
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Panel", tint = Color.Gray) },
                    label = { Text("Panel", color = Color.Gray) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Menu, contentDescription = "Pedidos", tint = colorPurpura) },
                    label = { Text("Pedidos", color = colorPurpura, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onIrAMenu,
                    icon = { Icon(Icons.Default.LocalCafe, contentDescription = "Menú", tint = Color.Gray) },
                    label = { Text("Menú", color = Color.Gray) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onIrAAjustes,
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = Color.Gray) },
                    label = { Text("Ajustes", color = Color.Gray) }
                )
            }
        },
        containerColor = Color(0xFFFAFAFA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                opcionesFiltro.forEach { (clave, etiqueta) ->
                    val seleccionada = clave == estadoActual
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { pedidosAdminViewModel.seleccionarEstado(clave) },
                        shape = RoundedCornerShape(20.dp),
                        color = if (seleccionada) colorPurpura else Color.White,
                        border = BorderStroke(
                            1.dp,
                            if (seleccionada) colorPurpura else Color(0xFFE5E7EB)
                        )
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = etiqueta,
                                color = if (seleccionada) Color.White else Color(0xFF6B7280),
                                fontSize = 13.sp,
                                fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (listaPedidos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay pedidos en este estado",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listaPedidos) { item ->
                        TarjetaPedidoAdmin(
                            item = item,
                            colorPurpura = colorPurpura,
                            onClick = { onPedidoClick(item.pedido.id) },
                            onAvanzar = { pedidosAdminViewModel.avanzar(item) },
                            onCancelar = { pedidosAdminViewModel.cancelar(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaPedidoAdmin(
    item: PedidoConDetalle,
    colorPurpura: Color,
    onClick: () -> Unit,
    onAvanzar: () -> Unit,
    onCancelar: () -> Unit
) {
    val clienteNombre = item.cliente.nombre.ifBlank { "Cliente #${item.pedido.clienteId}" }
    val resumenProductos = item.detalles.joinToString(" · ") { "${it.detalle.cantidad} x ${it.producto.nombre}" }

    val textoBotonAvanzar = when (item.pedido.estado) {
        "PENDIENTE" -> "Pasar a Preparando"
        "EN_PREPARACION" -> "Pasar a Listo"
        "LISTO" -> "Pasar a Entregado"
        else -> "Avanzar"
    }

    val horaRecojoText = item.pedido.horaRecojo.ifBlank { "10:30" }
    val notaTexto = item.pedido.nota.ifBlank { "" }

    val detallesInfo = buildList {
        add("Recojo $horaRecojoText")
        add("S/ %.2f".format(item.pedido.total))
        if (notaTexto.isNotBlank()) {
            add("Nota: $notaTexto")
        }
    }.joinToString("  ·  ")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFEEF2FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = colorPurpura,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "#%04d  %s".format(item.pedido.id, clienteNombre),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (resumenProductos.isNotBlank()) resumenProductos else "Pedido general",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                Surface(
                    color = when (item.pedido.estado) {
                        "PENDIENTE" -> Color(0xFFE0E7FF)
                        "EN_PREPARACION" -> Color(0xFFFEF3C7)
                        "LISTO" -> Color(0xFFDCFCE7)
                        else -> Color(0xFFF3F4F6)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = when (item.pedido.estado) {
                            "PENDIENTE" -> "Recibido"
                            "EN_PREPARACION" -> "Preparando"
                            "LISTO" -> "Listo"
                            else -> item.pedido.estado
                        },
                        color = when (item.pedido.estado) {
                            "PENDIENTE" -> Color(0xFF2563EB)
                            "EN_PREPARACION" -> Color(0xFFD97706)
                            "LISTO" -> Color(0xFF16A34A)
                            else -> Color.Gray
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = detallesInfo,
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onAvanzar,
                    modifier = Modifier.weight(1.2f),
                    colors = ButtonDefaults.buttonColors(containerColor = colorPurpura),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Text(
                        text = textoBotonAvanzar,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Button(
                    onClick = onCancelar,
                    modifier = Modifier.weight(0.8f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
                    )
                }
            }
        }
    }
}
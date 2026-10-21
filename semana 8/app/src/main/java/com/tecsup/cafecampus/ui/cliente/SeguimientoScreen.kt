package com.tecsup.cafecampus.ui.cliente

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(
    pedidosViewModel: PedidosClienteViewModel,
    onVolver: () -> Unit,
    onIrAMenu: () -> Unit,
    onIrAPedidos: () -> Unit
) {
    val pedidoConDetalle by pedidosViewModel.pedidoActual.collectAsState()
    val colorTema = Color(0xFFE65100)

    val estadoActual = pedidoConDetalle?.pedido?.estado ?: "PENDIENTE"
    val ordenEstados = listOf(
        "PENDIENTE" to ("Recibido" to "Tu pedido llegó a la cocina"),
        "EN_PREPARACION" to ("Preparando" to "Están cocinando tu pedido"),
        "LISTO" to ("Listo" to "Puedes pasar a recogerlo"),
        "ENTREGADO" to ("Entregado" to "¡Buen provecho!")
    )

    val indiceActual = when (estadoActual) {
        "PENDIENTE" -> 0
        "EN_PREPARACION" -> 1
        "LISTO" -> 2
        "ENTREGADO" -> 3
        else -> 0
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Seguimiento",
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
                    onClick = onIrAPedidos,
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Pedidos", tint = colorTema) },
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
        if (pedidoConDetalle == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = "Sin pedido",
                        tint = Color(0xFFD1D5DB),
                        modifier = Modifier.size(96.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay un pedido en seguimiento",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Selecciona un pedido desde tu historial o realiza uno nuevo en el menú.",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onIrAMenu,
                        colors = ButtonDefaults.buttonColors(containerColor = colorTema),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Ir al Menú", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = colorTema)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Pedido #%04d".format(pedidoConDetalle?.pedido?.id ?: 0),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Recojo a las 10:30",
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tiempo estimado: 10 min",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        ordenEstados.forEachIndexed { index, (_, info) ->
                            val (titulo, desc) = info
                            val esCompletado = index < indiceActual
                            val esActual = index == indiceActual

                            val colorAnimadoCircle by animateColorAsState(
                                targetValue = when {
                                    esCompletado -> Color(0xFF10B981)
                                    esActual -> Color(0xFFFFE0B2)
                                    else -> Color.Transparent
                                },
                                animationSpec = tween(durationMillis = 350),
                                label = "colorCircle"
                            )

                            val colorAnimadoLined by animateColorAsState(
                                targetValue = if (index < indiceActual) colorTema else Color(0xFFE5E7EB),
                                animationSpec = tween(durationMillis = 350),
                                label = "colorLine"
                            )

                            val sizeAnimadoCircle by animateDpAsState(
                                targetValue = if (esActual) 36.dp else 32.dp,
                                animationSpec = tween(durationMillis = 300),
                                label = "sizeCircle"
                            )

                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(sizeAnimadoCircle)
                                            .background(colorAnimadoCircle, CircleShape)
                                            .then(
                                                if (!esCompletado && !esActual) {
                                                    Modifier.border(2.dp, Color(0xFFD1D5DB), CircleShape)
                                                } else Modifier
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        when {
                                            esCompletado -> {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                            esActual -> {
                                                Box(
                                                    modifier = Modifier
                                                        .size(20.dp)
                                                        .background(colorTema, CircleShape)
                                                )
                                            }
                                        }
                                    }

                                    if (index < ordenEstados.size - 1) {
                                        Box(
                                            modifier = Modifier
                                                .width(2.dp)
                                                .height(36.dp)
                                                .background(colorAnimadoLined)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = titulo,
                                        fontSize = 15.sp,
                                        fontWeight = if (esActual || esCompletado) FontWeight.Bold else FontWeight.Normal,
                                        color = if (esActual || esCompletado) Color(0xFF1F2937) else Color(0xFF9CA3AF)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = desc,
                                        fontSize = 12.sp,
                                        color = Color(0xFF9CA3AF)
                                    )
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        val detalles = pedidoConDetalle?.detalles.orEmpty()
                        val resumenItems = if (detalles.isNotEmpty()) {
                            detalles.joinToString(" · ") {
                                "${it.detalle.cantidad} x ${it.producto.nombre}"
                            }
                        } else "Sin items"

                        Text(
                            text = resumenItems,
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Total S/ %.2f".format(pedidoConDetalle?.pedido?.total ?: 0.00),
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
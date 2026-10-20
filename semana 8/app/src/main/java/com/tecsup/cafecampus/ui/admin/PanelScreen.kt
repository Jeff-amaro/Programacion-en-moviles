package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel

@Composable
fun PanelScreen(
    panelAdminViewModel: PanelAdminViewModel,
    onIrAPedidos: () -> Unit,
    onIrAMenu: () -> Unit,
    onIrAAjustes: () -> Unit
) {
    val nuevosCount by panelAdminViewModel.nuevos.collectAsState()
    val preparandoCount by panelAdminViewModel.preparando.collectAsState()
    val listosCount by panelAdminViewModel.listos.collectAsState()
    val ventasHoyTotal by panelAdminViewModel.ventasHoy.collectAsState()
    val todosPedidos by panelAdminViewModel.todosLosPedidos.collectAsState()
    val nuevosPreview by panelAdminViewModel.nuevosPedidosPreview.collectAsState()

    val colorPurpura = Color(0xFF4F46E5)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Panel", tint = colorPurpura) },
                    label = { Text("Panel", color = colorPurpura, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onIrAPedidos,
                    icon = { Icon(Icons.Default.Menu, contentDescription = "Pedidos", tint = Color.Gray) },
                    label = { Text("Pedidos", color = Color.Gray) }
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
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Panel de control",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Cafetería · Hoy",
                            fontSize = 13.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TarjetaContador(
                        modifier = Modifier.weight(1f),
                        numero = nuevosCount,
                        etiqueta = "Nuevos",
                        colorFondo = Color(0xFFE8F2FF),
                        colorTexto = Color(0xFF2563EB)
                    )
                    TarjetaContador(
                        modifier = Modifier.weight(1f),
                        numero = preparandoCount,
                        etiqueta = "Preparando",
                        colorFondo = Color(0xFFFEF9C3),
                        colorTexto = Color(0xFFD97706)
                    )
                    TarjetaContador(
                        modifier = Modifier.weight(1f),
                        numero = listosCount,
                        etiqueta = "Listos",
                        colorFondo = Color(0xFFDCFCE7),
                        colorTexto = Color(0xFF16A34A)
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = colorPurpura)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Ventas del día",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "S/ %.2f".format(ventasHoyTotal),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${todosPedidos.size} pedidos",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color(0xFF374151))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Pedidos por hora",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GraficoBarrasHoras(colorPurpura = colorPurpura)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nuevos pedidos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Ver todos",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorPurpura,
                        modifier = Modifier.clickable { onIrAPedidos() }
                    )
                }
            }

            if (nuevosPreview.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay pedidos pendientes",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(nuevosPreview) { item ->
                    val nombreFormateado = formatearNombreCliente(item.cliente.nombre.ifBlank { "Cliente #${item.pedido.clienteId}" })
                    val detallesText = item.detalles.joinToString(" + ") { "${it.detalle.cantidad} x ${it.producto.nombre}" }
                    val subtitulo = if (detallesText.isNotBlank()) "$detallesText · 10:30" else "10:30"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, Color(0xFF374151))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "#%04d  %s".format(item.pedido.id, nombreFormateado),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = subtitulo,
                                    fontSize = 12.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { panelAdminViewModel.avanzarEstado(item) },
                                colors = ButtonDefaults.buttonColors(containerColor = colorPurpura),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                            ) {
                                Text("Preparar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatearNombreCliente(nombreCompleto: String): String {
    val partes = nombreCompleto.trim().split("\\s+".toRegex())
    return if (partes.size >= 2) {
        "${partes[0]} ${partes[1].first().uppercaseChar()}."
    } else {
        nombreCompleto
    }
}

@Composable
private fun TarjetaContador(
    modifier: Modifier = Modifier,
    numero: Int,
    etiqueta: String,
    colorFondo: Color,
    colorTexto: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$numero",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = etiqueta,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
        }
    }
}

@Composable
private fun GraficoBarrasHoras(colorPurpura: Color) {
    val datos = listOf(
        Triple("8h", 3, 0.35f),
        Triple("9h", 6, 0.60f),
        Triple("10h", 9, 0.90f),
        Triple("11h", 5, 0.50f),
        Triple("12h", 7, 0.70f)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        datos.forEachIndexed { index, (hora, cantidad, fraccion) ->
            val esMax = index == 2
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = "$cantidad",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .fillMaxHeight(fraccion)
                        .background(
                            if (esMax) colorPurpura else Color(0xFFC7D2FE),
                            RoundedCornerShape(8.dp)
                        )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = hora,
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF)
                )
            }
        }
    }
}
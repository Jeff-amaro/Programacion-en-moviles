package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.entity.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    producto: Producto,
    onAgregarAlCarrito: (cantidad: Int, nota: String) -> Unit,
    onVolver: () -> Unit
) {
    var cantidad by remember { mutableIntStateOf(1) }
    var nota by remember { mutableStateOf("") }

    val colorTema = Color(0xFFE65100)
    val precioTotal = producto.precio * cantidad

    val fondoHeader = when {
        producto.nombre.lowercase().contains("café") -> Color(0xFFEFEBE9)
        producto.nombre.lowercase().contains("sandwich") -> Color(0xFFFFF8E1)
        else -> Color(0xFFFFF3E0)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Detalle del producto",
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
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(fondoHeader, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    ProductoGrafico(
                        nombre = producto.nombre,
                        modifier = Modifier.fillMaxSize(0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = producto.nombre,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "S/ %.2f".format(producto.precio),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorTema
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        color = if (producto.disponible) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (producto.disponible) "Disponible" else "Agotado",
                            color = if (producto.disponible) Color(0xFF2E7D32) else Color(0xFFC62828),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "10 - 15 min",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = producto.descripcion,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cantidad",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    Surface(
                        color = Color(0xFFF9FAFB),
                        shape = RoundedCornerShape(25.dp),
                        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            IconButton(
                                onClick = { if (cantidad > 1) cantidad-- },
                                enabled = cantidad > 1 && producto.disponible,
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        if (cantidad > 1 && producto.disponible) Color(0xFFFFE0B2) else Color(0xFFF3F4F6),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Restar",
                                    tint = if (cantidad > 1 && producto.disponible) colorTema else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Text(
                                text = "$cantidad",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937)
                            )

                            IconButton(
                                onClick = { cantidad++ },
                                enabled = producto.disponible,
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        if (producto.disponible) colorTema else Color(0xFFF3F4F6),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Sumar",
                                    tint = if (producto.disponible) Color.White else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Nota para la cocina",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = nota,
                    onValueChange = { nota = it },
                    placeholder = { Text("Sin ají, por favor", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    enabled = producto.disponible,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorTema,
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )
            }

            Column(
                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    color = Color(0xFFFFF8F0),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "S/ %.2f".format(precioTotal),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorTema
                        )
                    }
                }

                Button(
                    onClick = {
                        if (producto.disponible) {
                            onAgregarAlCarrito(cantidad, nota)
                            onVolver()
                        }
                    },
                    enabled = producto.disponible,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorTema),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (producto.disponible) "Agregar al pedido" else "Agotado",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
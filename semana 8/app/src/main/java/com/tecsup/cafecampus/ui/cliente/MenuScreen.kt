package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.R
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun ProductoGrafico(nombre: String, modifier: Modifier = Modifier) {
    val lower = nombre.lowercase()

    val imagenRes = when {
        lower.contains("americano") -> R.drawable.cafe_americano
        lower.contains("cappuccino") -> R.drawable.cappuccino
        lower.contains("empanada") -> R.drawable.empanada_de_carne
        lower.contains("latte") -> R.drawable.latte
        lower.contains("sandwich") || lower.contains("sándwich") || lower.contains("pollo") -> R.drawable.sandwich_de_pollo
        lower.contains("torta") || lower.contains("chocolate") -> R.drawable.torta_de_chocolate
        else -> R.drawable.ic_launcher_foreground
    }

    Image(
        painter = painterResource(id = imagenRes),
        contentDescription = nombre,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel,
    sesionViewModel: SesionViewModel,
    onProductoClick: (Producto) -> Unit,
    onCarritoClick: () -> Unit,
    onPedidosClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    val productos by menuViewModel.productos.collectAsState()
    val carrito by menuViewModel.carrito.collectAsState()
    val usuarioActual by sesionViewModel.usuarioActual.collectAsState()
    val busqueda by menuViewModel.busqueda.collectAsState()
    val categoriaSeleccionada by menuViewModel.categoriaSeleccionada.collectAsState()
    val recibirPedidos by sesionViewModel.recibirPedidos.collectAsState()

    val colorTema = Color(0xFFE65100)
    val totalItemsCarrito = carrito.sumOf { it.cantidad }

    val categorias = listOf("Todo", "Desayunos", "Almuerzos", "Bebidas")

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Menú", tint = colorTema) },
                    label = { Text("Menú", color = colorTema, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onPedidosClick,
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Pedidos", tint = Color.Gray) },
                    label = { Text("Pedidos", color = Color.Gray) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onPerfilClick,
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
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¡Hola, ${usuarioActual?.nombre?.split(" ")?.firstOrNull() ?: "Juan"}!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "¿Qué se te antoja hoy?",
                        fontSize = 14.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }

                BadgedBox(
                    badge = {
                        if (totalItemsCarrito > 0) {
                            Badge(
                                containerColor = colorTema,
                                contentColor = Color.White
                            ) {
                                Text("$totalItemsCarrito")
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onCarritoClick,
                        modifier = Modifier
                            .background(Color(0xFFF3F4F6), CircleShape)
                            .size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingBag,
                            contentDescription = "Carrito",
                            tint = Color(0xFF1F2937)
                        )
                    }
                }
            }

            if (!recibirPedidos) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFEF4444))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "☕ Cafetería Cerrada",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF991B1B),
                            fontSize = 13.sp
                        )
                        Text(
                            text = "— No se están recibiendo nuevos pedidos.",
                            color = Color(0xFF991B1B),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = busqueda,
                onValueChange = { menuViewModel.buscarProducto(it) },
                placeholder = { Text("Buscar en el menú...", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF9CA3AF)
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE5E7EB),
                    unfocusedBorderColor = Color(0xFFF3F4F6),
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFFAFAFA)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { cat ->
                    val seleccionada = cat == categoriaSeleccionada
                    Box(
                        modifier = Modifier
                            .background(
                                if (seleccionada) colorTema else Color(0xFFF3F4F6),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { menuViewModel.seleccionarCategoria(cat) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (seleccionada) Color.White else Color(0xFF6B7280),
                            fontSize = 13.sp,
                            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Populares hoy",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProductoClick(producto) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                ProductoGrafico(
                                    nombre = producto.nombre,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = producto.nombre,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937),
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "S/ %.2f".format(producto.precio),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorTema
                                )

                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(if (recibirPedidos) colorTema else Color(0xFF9CA3AF), RoundedCornerShape(8.dp))
                                        .clickable { onProductoClick(producto) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Agregar",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
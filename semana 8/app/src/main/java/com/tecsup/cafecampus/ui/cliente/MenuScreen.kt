package com.tecsup.cafecampus.ui.cliente

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
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun ProductoGrafico(nombre: String, modifier: Modifier = Modifier) {
    val lower = nombre.lowercase()
    when {
        lower.contains("menú") || lower.contains("menu") -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(54.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = Color(0xFFFAF0E6)
                        ) {}
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(10.dp).background(Color(0xFFFFA000), CircleShape))
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Box(modifier = Modifier.size(8.dp).background(Color(0xFFE53935), CircleShape))
                                Box(modifier = Modifier.size(8.dp).background(Color(0xFF43A047), CircleShape))
                            }
                        }
                    }
                }
            }
        }
        lower.contains("sandwich") || lower.contains("sándwich") || lower.contains("empanada") -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Surface(modifier = Modifier.width(42.dp).height(7.dp), shape = RoundedCornerShape(3.dp), color = Color(0xFFF57C00)) {}
                    Surface(modifier = Modifier.width(46.dp).height(5.dp), shape = RoundedCornerShape(2.dp), color = Color(0xFF43A047)) {}
                    Surface(modifier = Modifier.width(42.dp).height(7.dp), shape = RoundedCornerShape(3.dp), color = Color(0xFFF57C00)) {}
                }
            }
        }
        lower.contains("café") || lower.contains("cappuccino") || lower.contains("latte") -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.LocalCafe,
                    contentDescription = null,
                    tint = Color(0xFF6D4C41),
                    modifier = Modifier.size(42.dp)
                )
            }
        }
        lower.contains("jugo") || lower.contains("bebida") -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.LocalDrink,
                    contentDescription = null,
                    tint = Color(0xFFFB8C00),
                    modifier = Modifier.size(42.dp)
                )
            }
        }
        else -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = null,
                    tint = Color(0xFFE65100),
                    modifier = Modifier.size(38.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel,
    sesionViewModel: SesionViewModel,
    onProductoClick: (Producto) -> Unit,
    onCarritoClick: () -> Unit,
    onPedidosClick: () -> Unit
) {
    val productos by menuViewModel.productos.collectAsState()
    val carrito by menuViewModel.carrito.collectAsState()
    val usuarioActual by sesionViewModel.usuarioActual.collectAsState()

    var busqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todo") }

    val colorTema = Color(0xFFE65100)
    val totalItemsCarrito = carrito.sumOf { it.cantidad }

    val categorias = listOf("Todo", "Desayunos", "Almuerzos", "Bebidas")

    val productosFiltrados = productos.filter { producto ->
        val coincideBusqueda = producto.nombre.contains(busqueda, ignoreCase = true)
        val coincideCategoria = if (categoriaSeleccionada == "Todo") true else producto.categoria.equals(categoriaSeleccionada, ignoreCase = true)
        coincideBusqueda && coincideCategoria
    }

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

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
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
                            .clickable { categoriaSeleccionada = cat }
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
                items(productosFiltrados) { producto ->
                    val fondoBox = when {
                        producto.nombre.lowercase().contains("café") -> Color(0xFFEFEBE9)
                        producto.nombre.lowercase().contains("sandwich") -> Color(0xFFFFF8E1)
                        else -> Color(0xFFFFF3E0)
                    }

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
                                    .background(fondoBox, RoundedCornerShape(12.dp)),
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
                                        .background(colorTema, RoundedCornerShape(8.dp))
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
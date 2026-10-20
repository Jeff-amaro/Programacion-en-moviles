package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.ui.cliente.ProductoGrafico
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(
    menuAdminViewModel: MenuAdminViewModel,
    onAgregarProducto: () -> Unit,
    onEditarProducto: (Producto) -> Unit,
    onIrAPanel: () -> Unit,
    onIrAPedidos: () -> Unit,
    onIrAAjustes: () -> Unit,
    onVolver: () -> Unit
) {
    val productos by menuAdminViewModel.productos.collectAsState()
    val categoriaActual by menuAdminViewModel.categoriaSeleccionada.collectAsState()

    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }
    val colorPurpura = Color(0xFF4F46E5)

    val categorias = listOf("Todo", "Desayunos", "Almuerzos", "Bebidas", "Snacks")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Gestión del menú",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarProducto,
                containerColor = colorPurpura,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto")
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onIrAPanel,
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Panel", tint = Color.Gray) },
                    label = { Text("Panel", color = Color.Gray) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onIrAPedidos,
                    icon = { Icon(Icons.Default.Menu, contentDescription = "Pedidos", tint = Color.Gray) },
                    label = { Text("Pedidos", color = Color.Gray) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.LocalCafe, contentDescription = "Menú", tint = colorPurpura) },
                    label = { Text("Menú", color = colorPurpura, fontWeight = FontWeight.Bold) }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categorias) { cat ->
                    val seleccionada = cat == categoriaActual
                    Surface(
                        modifier = Modifier.clickable { menuAdminViewModel.seleccionarCategoria(cat) },
                        shape = RoundedCornerShape(20.dp),
                        color = if (seleccionada) colorPurpura else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (seleccionada) colorPurpura else Color(0xFF374151)
                        )
                    ) {
                        Text(
                            text = cat,
                            color = if (seleccionada) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos) { producto ->
                    TarjetaProductoAdmin(
                        producto = producto,
                        colorPurpura = colorPurpura,
                        onEditar = { onEditarProducto(producto) },
                        onEliminar = { productoAEliminar = producto },
                        onToggleDisponible = { menuAdminViewModel.alternarDisponibilidad(producto) }
                    )
                }
            }
        }

        productoAEliminar?.let { prod ->
            AlertDialog(
                onDismissRequest = { productoAEliminar = null },
                title = { Text("¿Eliminar producto?", fontWeight = FontWeight.Bold) },
                text = { Text("¿Estás seguro de que deseas eliminar '${prod.nombre}'? Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            menuAdminViewModel.eliminar(prod)
                            productoAEliminar = null
                        }
                    ) {
                        Text("Eliminar", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { productoAEliminar = null }) {
                        Text("Cancelar", color = Color(0xFF6B7280))
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
private fun TarjetaProductoAdmin(
    producto: Producto,
    colorPurpura: Color,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    onToggleDisponible: () -> Unit
) {
    val fondoBox = when {
        producto.nombre.lowercase().contains("café") -> Color(0xFFEFEBE9)
        producto.nombre.lowercase().contains("sandwich") || producto.nombre.lowercase().contains("sándwich") -> Color(0xFFFFF8E1)
        else -> Color(0xFFFFF3E0)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF374151)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(fondoBox, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    ProductoGrafico(
                        nombre = producto.nombre,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Column {
                    Text(
                        text = producto.nombre,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${producto.categoria} · S/ %.2f".format(producto.precio),
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = if (producto.disponible) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.clickable { onToggleDisponible() }
                    ) {
                        Text(
                            text = if (producto.disponible) "Disponible" else "Agotado",
                            color = if (producto.disponible) Color(0xFF16A34A) else Color(0xFFDC2626),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onEditar) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = colorPurpura,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
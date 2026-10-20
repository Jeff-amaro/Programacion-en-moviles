package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.ui.cliente.ProductoGrafico
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    menuAdminViewModel: MenuAdminViewModel,
    productoExistente: Producto?,
    onGuardadoExitoso: () -> Unit,
    onVolver: () -> Unit
) {
    val esEdicion = productoExistente != null

    var nombre by remember { mutableStateOf(productoExistente?.nombre ?: "") }
    var categoria by remember { mutableStateOf(productoExistente?.categoria ?: "Desayunos") }
    var precioTexto by remember { mutableStateOf(productoExistente?.precio?.toString() ?: "") }
    var descripcion by remember { mutableStateOf(productoExistente?.descripcion ?: "") }
    var disponible by remember { mutableStateOf(productoExistente?.disponible ?: true) }

    var errorNombre by remember { mutableStateOf(false) }
    var errorPrecio by remember { mutableStateOf(false) }

    val colorPurpura = Color(0xFF4F46E5)
    val categoriasDisponibles = listOf("Desayunos", "Almuerzos", "Bebidas", "Snacks")
    var menuCategoriaExpandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (esEdicion) "Editar producto" else "Nuevo producto",
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
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                        border = BorderStroke(0.dp, Color.Transparent)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ProductoGrafico(
                                nombre = nombre.ifBlank { "Producto" },
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Imagen del producto",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD97706)
                            )
                            Text(
                                text = "Toca para elegir color",
                                fontSize = 11.sp,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            errorNombre = false
                        },
                        label = { Text("Nombre del producto") },
                        isError = errorNombre,
                        supportingText = {
                            if (errorNombre) Text("El nombre es obligatorio", color = Color.Red)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorPurpura,
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )
                }

                item {
                    ExposedDropdownMenuBox(
                        expanded = menuCategoriaExpandido,
                        onExpandedChange = { menuCategoriaExpandido = !menuCategoriaExpandido }
                    ) {
                        OutlinedTextField(
                            value = categoria,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuCategoriaExpandido) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorPurpura,
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = menuCategoriaExpandido,
                            onDismissRequest = { menuCategoriaExpandido = false }
                        ) {
                            categoriasDisponibles.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        categoria = cat
                                        menuCategoriaExpandido = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = precioTexto,
                        onValueChange = {
                            precioTexto = it
                            errorPrecio = false
                        },
                        label = { Text("Precio (S/)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = errorPrecio,
                        supportingText = {
                            if (errorPrecio) Text("Ingrese un precio mayor a 0", color = Color.Red)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorPurpura,
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )
                }

                item {
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorPurpura,
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Disponible en el menú",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937)
                                )
                                Text(
                                    text = "Los estudiantes podrán pedirlo",
                                    fontSize = 12.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                            Switch(
                                checked = disponible,
                                onCheckedChange = { disponible = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = colorPurpura
                                )
                            )
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
                        val precio = precioTexto.toDoubleOrNull() ?: 0.0
                        var valido = true

                        if (nombre.isBlank()) {
                            errorNombre = true
                            valido = false
                        }
                        if (precio <= 0) {
                            errorPrecio = true
                            valido = false
                        }

                        if (valido) {
                            val nuevoProducto = Producto(
                                id = productoExistente?.id ?: 0,
                                nombre = nombre.trim(),
                                categoria = categoria,
                                precio = precio,
                                descripcion = descripcion.trim(),
                                disponible = disponible,
                                icono = productoExistente?.icono ?: "coffee"
                            )
                            menuAdminViewModel.guardar(nuevoProducto)
                            onGuardadoExitoso()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorPurpura),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Guardar producto",
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
                    border = BorderStroke(1.dp, colorPurpura),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        color = colorPurpura,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
package com.amaro.lab04carritotecsup.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amaro.lab04carritotecsup.data.Producto
import com.amaro.lab04carritotecsup.screens.AppDrawer
import com.amaro.lab04carritotecsup.screens.PantallaFavoritos
import com.amaro.lab04carritotecsup.screens.PantallaPerfil
import com.amaro.lab04carritotecsup.screens.TarjetaProducto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var destinoActual by remember { mutableStateOf("inicio") }

    var productos by remember {
        mutableStateOf(
            listOf(
                Producto(id = 1, nombre = "Audífonos", precio = 89.00, cantidad = 1, esFavorito = false),
                Producto(id = 2, nombre = "Smartwatch", precio = 199.00, cantidad = 1, esFavorito = false),
                Producto(id = 3, nombre = "Funda celular", precio = 25.00, cantidad = 2, esFavorito = false)
            )
        )
    }

    val cantidadFavoritos = productos.count { it.esFavorito }

    val subtituloTopBar = when (destinoActual) {
        "favoritos" -> "Mis Favoritos"
        "perfil" -> "Perfil de Usuario"
        "pedidos" -> "Mis Pedidos Realizados"
        else -> "Más vendidos"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                destinoActual = destinoActual,
                cantidadFavoritos = cantidadFavoritos,
                onNavegar = { nuevoDestino ->
                    destinoActual = nuevoDestino
                    scope.launch { drawerState.close() }
                },
                onCerrarSesion = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(text = "TECSUP Store", fontWeight = FontWeight.Bold)
                            Text(
                                text = subtituloTopBar,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (destinoActual) {
                    "favoritos" -> {
                        PantallaFavoritos(
                            productosFavoritos = productos.filter { it.esFavorito },
                            onEliminar = { p -> productos = productos.filter { it.id != p.id } },
                            onToggleFavorito = { p ->
                                productos = productos.map { item ->
                                    if (item.id == p.id) item.copy(esFavorito = !item.esFavorito) else item
                                }
                            },
                            onEditarProducto = { pEdit ->
                                productos = productos.map { item ->
                                    if (item.id == pEdit.id) pEdit else item
                                }
                            }
                        )
                    }

                    "perfil" -> {
                        PantallaPerfil()
                    }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(productos, key = { it.id }) { producto ->
                                TarjetaProducto(
                                    producto = producto,
                                    onEliminar = { productos = productos.filter { it.id != producto.id } },
                                    onToggleFavorito = {
                                        productos = productos.map { item ->
                                            if (item.id == producto.id) item.copy(esFavorito = !item.esFavorito) else item
                                        }
                                    },
                                    onEditarProducto = { productoEditado ->
                                        productos = productos.map { item ->
                                            if (item.id == productoEditado.id) productoEditado else item
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
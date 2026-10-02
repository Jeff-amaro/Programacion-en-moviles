package com.amaro.lab04carritotecsup.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.amaro.lab04carritotecsup.data.Producto
import com.amaro.lab04carritotecsup.screens.PantallaCarrito
import com.amaro.lab04carritotecsup.screens.PantallaFavoritos
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var pantallaActual by remember { mutableStateOf("carrito") }

    val productos = remember { mutableStateListOf<Producto>() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                pantallaActual = pantallaActual,
                onNavegar = { pantallaActual = it },
                onCerrarDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(if (pantallaActual == "carrito") "Mi Carrito" else "Mis Favoritos")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            if (pantallaActual == "carrito") {
                PantallaCarrito(
                    productos = productos,
                    onAgregarProducto = { nuevoProducto -> productos.add(nuevoProducto) },
                    onEliminarProducto = { producto -> productos.remove(producto) },
                    onToggleFavorito = { producto ->
                        val index = productos.indexOf(producto)
                        if (index != -1) {
                            productos[index] = producto.copy(esFavorito = !producto.esFavorito)
                        }
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            } else {
                PantallaFavoritos(
                    productosFavoritos = productos.filter { it.esFavorito },
                    onEliminar = { producto -> productos.remove(producto) },
                    onToggleFavorito = { producto ->
                        val index = productos.indexOf(producto)
                        if (index != -1) {
                            productos[index] = producto.copy(esFavorito = !producto.esFavorito)
                        }
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
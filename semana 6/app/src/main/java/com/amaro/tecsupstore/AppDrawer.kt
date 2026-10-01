package com.amaro.tecsupstore

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

enum class DrawerScreen {
    Inicio, Perfil, Configuracion
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(content: @Composable () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(DrawerScreen.Inicio) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") },
                    selected = currentScreen == DrawerScreen.Inicio,
                    onClick = {
                        currentScreen = DrawerScreen.Inicio
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Perfil") },
                    selected = currentScreen == DrawerScreen.Perfil,
                    onClick = {
                        currentScreen = DrawerScreen.Perfil
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Configuración") },
                    selected = currentScreen == DrawerScreen.Configuracion,
                    onClick = {
                        currentScreen = DrawerScreen.Configuracion
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            when (currentScreen) {
                                DrawerScreen.Inicio -> "Tecsup Store - Productos"
                                DrawerScreen.Perfil -> "Perfil del Usuario"
                                DrawerScreen.Configuracion -> "Configuración"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    DrawerScreen.Inicio -> content()
                    DrawerScreen.Perfil -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pantalla de Perfil",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                    DrawerScreen.Configuracion -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pantalla de Configuración",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.amaro.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF6750A4))
                        .padding(24.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8DEF8)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF4A148C),
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Usuario Tecsup",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "alumno@tecsup.edu.pe",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE8DEF8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            tint = if (currentScreen == DrawerScreen.Inicio) Color(0xFF6750A4) else Color.Gray
                        )
                    },
                    label = { Text("Inicio") },
                    selected = currentScreen == DrawerScreen.Inicio,
                    onClick = {
                        currentScreen = DrawerScreen.Inicio
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = if (currentScreen == DrawerScreen.Perfil) Color(0xFF6750A4) else Color.Gray
                        )
                    },
                    label = { Text("Perfil") },
                    selected = currentScreen == DrawerScreen.Perfil,
                    onClick = {
                        currentScreen = DrawerScreen.Perfil
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = null,
                            tint = if (currentScreen == DrawerScreen.Configuracion) Color(0xFF6750A4) else Color.Gray
                        )
                    },
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
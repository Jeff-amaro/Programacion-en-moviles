package com.amaro.clinica_salud.clinica

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicaApp() {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val listaCitas = remember {
        mutableStateListOf(
            Cita(1, "Dra. Ana Torres", "Viernes 27", "10:30 am", "Confirmada"),
            Cita(2, "Dr. Luis Vega", "Miércoles 15", "3:00 pm", "Completada")
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Clínica Salud+",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("inicio") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Mis Citas") },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("mis_citas")
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Clínica Salud+") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "inicio",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("inicio") {
                    Text("Pantalla de Inicio (En construcción)", modifier = Modifier.padding(16.dp))
                }

                composable(
                    route = "detalle/{doctorId}",
                    arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
                ) { backStack ->
                    val doctorId = backStack.arguments?.getInt("doctorId") ?: 0
                    Text("Detalle del Doctor ID: $doctorId", modifier = Modifier.padding(16.dp))
                }

                composable(
                    route = "agendar/{doctorId}",
                    arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
                ) { backStack ->
                    val doctorId = backStack.arguments?.getInt("doctorId") ?: 0
                    Text("Agendar cita para Doctor ID: $doctorId", modifier = Modifier.padding(16.dp))
                }

                composable(
                    route = "confirmacion/{doctorName}/{fecha}/{hora}",
                    arguments = listOf(
                        navArgument("doctorName") { type = NavType.StringType },
                        navArgument("fecha") { type = NavType.StringType },
                        navArgument("hora") { type = NavType.StringType }
                    )
                ) { backStack ->
                    val doc = backStack.arguments?.getString("doctorName") ?: ""
                    Text("Cita Confirmada con $doc", modifier = Modifier.padding(16.dp))
                }

                composable("mis_citas") {
                    Text("Lista de Mis Citas (${listaCitas.size} citas)", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
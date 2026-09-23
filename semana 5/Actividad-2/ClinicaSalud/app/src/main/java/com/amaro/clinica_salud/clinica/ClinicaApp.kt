package com.amaro.clinica_salud.clinica

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import com.amaro.clinica_salud.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicaApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val listaCitas = remember {
        mutableStateListOf(
            Cita(1, "Dra. Ana Torres", "Viernes 27", "10:30 am", "Confirmada"),
            Cita(2, "Dr. Luis Vega", "Miércoles 15", "3:00 pm", "Completada")
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Encabezado del Perfil
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(52.dp),
                            shape = CircleShape,
                            color = Color(0xFFEDE7F6)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "JA",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF4A148C)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Jeff Amaro",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "Paciente",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Línea divisora fina
                    HorizontalDivider(
                        color = Color(0xFFE0E0E0),
                        thickness = 1.dp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Opción 1: Inicio
                    val isInicioSelected = currentRoute == "inicio"
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Inicio",
                                fontWeight = if (isInicioSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        },
                        icon = { DrawerCircleIcon(isSelected = isInicioSelected) },
                        selected = isInicioSelected,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("inicio") {
                                popUpTo("inicio") { inclusive = true }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color(0xFFF3E8F4),
                            unselectedContainerColor = Color.Transparent,
                            selectedTextColor = Color(0xFF4A148C),
                            unselectedTextColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // Opción 2: Mis citas
                    val isCitasSelected = currentRoute == "mis_citas"
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Mis citas",
                                fontWeight = if (isCitasSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        },
                        icon = { DrawerCircleIcon(isSelected = isCitasSelected) },
                        selected = isCitasSelected,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("mis_citas")
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color(0xFFF3E8F4),
                            unselectedContainerColor = Color.Transparent,
                            selectedTextColor = Color(0xFF4A148C),
                            unselectedTextColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // Opción 3: Historial médico
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Historial médico",
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        },
                        icon = { DrawerCircleIcon(isSelected = false) },
                        selected = false,
                        onClick = { scope.launch { drawerState.close() } },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color(0xFFF3E8F4),
                            unselectedContainerColor = Color.Transparent,
                            selectedTextColor = Color(0xFF4A148C),
                            unselectedTextColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // Opción 4: Perfil
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Perfil",
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        },
                        icon = { DrawerCircleIcon(isSelected = false) },
                        selected = false,
                        onClick = { scope.launch { drawerState.close() } },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color(0xFFF3E8F4),
                            unselectedContainerColor = Color.Transparent,
                            selectedTextColor = Color(0xFF4A148C),
                            unselectedTextColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (currentRoute == "inicio") {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = "Clínica Salud+",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Hola, Jeff Amaro",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFE1BEE7)
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menú",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF4A148C)
                        )
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "inicio",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("inicio") { HomeScreen(navController = navController) }
                composable(
                    route = "detalle/{doctorId}",
                    arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
                ) { backStack ->
                    val doctorId = backStack.arguments?.getInt("doctorId") ?: 0
                    DetailScreen(doctorId = doctorId, navController = navController)
                }
                composable(
                    route = "agendar/{doctorId}",
                    arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
                ) { backStack ->
                    val doctorId = backStack.arguments?.getInt("doctorId") ?: 0
                    ScheduleScreen(doctorId = doctorId, navController = navController)
                }
                composable(
                    route = "confirmacion/{doctorName}/{fecha}/{hora}",
                    arguments = listOf(
                        navArgument("doctorName") { type = NavType.StringType },
                        navArgument("fecha") { type = NavType.StringType },
                        navArgument("hora") { type = NavType.StringType }
                    )
                ) { backStack ->
                    val doctorName = backStack.arguments?.getString("doctorName") ?: ""
                    val fecha = backStack.arguments?.getString("fecha") ?: ""
                    val hora = backStack.arguments?.getString("hora") ?: ""
                    ConfirmationScreen(
                        doctorName = doctorName,
                        fecha = fecha,
                        hora = hora,
                        navController = navController
                    )
                }
                composable("mis_citas") { MisCitasScreen(listaCitas = listaCitas) }
            }
        }
    }
}

// Componente para dibujar el ícono de círculo idéntico a la guía
@Composable
fun DrawerCircleIcon(isSelected: Boolean) {
    Surface(
        modifier = Modifier.size(20.dp),
        shape = CircleShape,
        color = Color.Transparent,
        border = BorderStroke(
            width = 2.dp,
            color = if (isSelected) Color(0xFF4A148C) else Color.DarkGray
        )
    ) {}
}
package com.amaro.tecsupfit

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amaro.tecsupfit.screen.*

sealed class BottomNavItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : BottomNavItem("home", "Inicio", Icons.Default.Home)
    object Reservas : BottomNavItem("reservas", "Reservas", Icons.Default.List)
    object Perfil : BottomNavItem("perfil", "Perfil", Icons.Default.Person)
}

@Composable
fun TecsupFitApp() {
    val navController = rememberNavController()
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Reservas,
        BottomNavItem.Perfil
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in navItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF00684A),
                                selectedTextColor = Color(0xFF00684A),
                                indicatorColor = Color(0xFFE8F5E9)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    onClaseClick = { claseId ->
                        navController.navigate("detail/$claseId")
                    }
                )
            }
            composable(
                route = "detail/{claseId}",
                arguments = listOf(navArgument("claseId") { type = NavType.StringType })
            ) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getString("claseId") ?: ""
                DetailScreen(
                    claseId = claseId,
                    onBackClick = { navController.popBackStack() },
                    onReservarClick = { nombre, horario ->
                        navController.navigate("confirmation/$nombre/$horario")
                    }
                )
            }
            composable(
                route = "confirmation/{claseNombre}/{horario}",
                arguments = listOf(
                    navArgument("claseNombre") { type = NavType.StringType },
                    navArgument("horario") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val claseNombre = backStackEntry.arguments?.getString("claseNombre") ?: ""
                val horario = backStackEntry.arguments?.getString("horario") ?: ""
                ConfirmationScreen(
                    claseNombre = claseNombre,
                    horario = horario,
                    onVerReservasClick = {
                        navController.navigate("reservas") {
                            popUpTo("home")
                        }
                    }
                )
            }
            composable("reservas") {
                ReservasScreen()
            }
            composable("perfil") {
                PerfilScreen()
            }
        }
    }
}
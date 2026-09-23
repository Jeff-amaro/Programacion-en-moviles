package com.tuapp.navlab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tuapp.navlab.screens.DetailScreen
import com.tuapp.navlab.screens.HomeScreen
import com.tuapp.navlab.screens.ListScreen
import com.tuapp.navlab.screens.LoginScreen
import com.tuapp.navlab.screens.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // 1. Portal Académico (Login)
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        // 2. Menú de Bienvenida (Juan León)
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        // 3. Directorio de alumnos
        composable(Screen.List.route) {
            ListScreen(navController)
        }

        // 4. Expediente / Detalle del alumno
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId")
            DetailScreen(navController = navController, itemId = itemId)
        }

        // 5. Mi Perfil Académico
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
    }
}
package com.amaro.clinica_salud.clinica

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

@Composable
fun ClinicaApp() {
    val navController = rememberNavController()

    val listaCitas = remember {
        mutableStateListOf(
            Cita(1, "Dra. Ana Torres", "Viernes 27", "10:30 am", "Confirmada"),
            Cita(2, "Dr. Luis Vega", "Miércoles 15", "3:00 pm", "Completada")
        )
    }

    NavHost(
        navController = navController,
        startDestination = "inicio" // Pantalla inicial al abrir la app
    ) {
        // ruta de inicio
        composable("inicio") {
            // Se implementará en el siguiente paso
            Text("Pantalla de Inicio (En construcción)")
        }

        // Rruta donde el medico recibe el id
        composable(
            route = "detalle/{doctorId}",
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStack ->
            val doctorId = backStack.arguments?.getInt("doctorId") ?: 0
            Text("Detalle del Doctor ID: $doctorId")
        }

        // ruta de agendar cita
        composable(
            route = "agendar/{doctorId}",
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStack ->
            val doctorId = backStack.arguments?.getInt("doctorId") ?: 0
            Text("Agendar cita para Doctor ID: $doctorId")
        }

        // ruta de confirma
        composable(
            route = "confirmacion/{doctorName}/{fecha}/{hora}",
            arguments = listOf(
                navArgument("doctorName") { type = NavType.StringType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { backStack ->
            val doc = backStack.arguments?.getString("doctorName") ?: ""
            Text("Cita Confirmada con $doc")
        }

        // ruta de citas
        composable("mis_citas") {
            Text("Lista de Mis Citas (${listaCitas.size} citas)")
        }
    }
}
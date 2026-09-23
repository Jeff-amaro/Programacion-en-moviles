package com.amaro.tecsupfit

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.amaro.tecsupfit.screen.*

@Composable
fun TecsupFitApp() {
    var pantallaActual by remember { mutableStateOf("inicio") }
    var claseIdSeleccionada by remember { mutableStateOf<String?>(null) }

    val tabs = listOf(
        Triple("inicio", "Inicio", Icons.Default.Home),
        Triple("reservas", "Reservas", Icons.Default.CalendarToday),
        Triple("rutinas", "Rutinas", Icons.Default.FitnessCenter),
        Triple("perfil", "Perfil", Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            if (claseIdSeleccionada == null) {
                NavigationBar(containerColor = Color.White) {
                    tabs.forEach { (route, label, icon) ->
                        NavigationBarItem(
                            selected = pantallaActual == route,
                            onClick = { pantallaActual = route },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) },
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
        Surface(modifier = Modifier.padding(innerPadding)) {
            if (claseIdSeleccionada != null) {
                DetailScreen(
                    claseId = claseIdSeleccionada!!,
                    onBackClick = { claseIdSeleccionada = null },
                    onReservarClick = { _, _ ->
                        claseIdSeleccionada = null
                        pantallaActual = "reservas"
                    }
                )
            } else {
                when (pantallaActual) {
                    "inicio" -> HomeScreen(onClaseClick = { id -> claseIdSeleccionada = id })
                    "reservas" -> ReservasScreen()
                    "rutinas" -> RutinasScreen()
                    "perfil" -> PerfilScreen()
                }
            }
        }
    }
}
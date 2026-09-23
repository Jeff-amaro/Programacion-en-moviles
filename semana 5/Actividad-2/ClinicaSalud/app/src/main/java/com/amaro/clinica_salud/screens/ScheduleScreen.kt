package com.amaro.clinica_salud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amaro.clinica_salud.clinica.*

data class FechaItem(
    val diaNombre: String,
    val diaNumero: String,
    val textoCompleto: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    doctorId: Int,
    navController: NavController
) {
    val doctor = listaDoctores.find { it.id == doctorId }

    val fechas = listOf(
        FechaItem("Jue", "26", "Jueves 26"),
        FechaItem("Vie", "27", "Viernes 27"),
        FechaItem("Sáb", "28", "Sábado 28")
    )
    val horas = listOf("9:00", "10:30", "3:00")

    var fechaSeleccionada by remember { mutableStateOf(fechas[1]) } // "Vie 27" por defecto
    var horaSeleccionada by remember { mutableStateOf("10:30") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Sección de Fecha
                Text(
                    text = "Selecciona fecha",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    fechas.forEach { item ->
                        val isSelected = fechaSeleccionada == item
                        Box(
                            modifier = Modifier
                                .width(75.dp)
                                .height(68.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) Color(0xFF4A148C) else Color(0xFFF6F0F8))
                                .clickable { fechaSeleccionada = item },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = item.diaNombre,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color.White else Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.diaNumero,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Sección de Hora
                Text(
                    text = "Selecciona hora",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    horas.forEach { hora ->
                        val isSelected = horaSeleccionada == hora
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) Color(0xFF4A148C) else Color(0xFFF6F0F8))
                                .clickable { horaSeleccionada = hora }
                                .padding(horizontal = 22.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = hora,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color.Black
                            )
                        }
                    }
                }
            }

            // Botón de Confirmación
            Button(
                onClick = {
                    val doctorName = doctor?.nombre ?: "Dra. Ana Torres"
                    val horaFinal = if (horaSeleccionada == "3:00") "3:00 pm" else "${horaSeleccionada} am"
                    navController.navigate("confirmacion/$doctorName/${fechaSeleccionada.textoCompleto}/$horaFinal")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A148C)
                )
            ) {
                Text(
                    text = "Confirmar cita",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }
    }
}
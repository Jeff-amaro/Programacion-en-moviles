package com.amaro.tecsupfit.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Rutina(val id: String, val titulo: String, val ejercicios: List<String>, val nivel: String)

@Composable
fun RutinasScreen() {
    val listaRutinas = remember {
        listOf(
            Rutina("1", "Rutina Tren Superior", listOf("Press banca 4x10", "Remo con barra 4x12", "Flexiones 3x15"), "Intermedio"),
            Rutina("2", "Rutina Pierna & Core", listOf("Sentadillas 4x10", "Prensa 3x12", "Plancha abdominal 3x1min"), "Avanzado"),
            Rutina("3", "Cardio HIiT Express", listOf("Jumping Jacks 4x45s", "Burpees 4x30s", "Escaladores 4x45s"), "Principiante")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Rutinas de entrenamiento", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(listaRutinas) { rutina ->
                var completada by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (completada) Color(0xFFE8F5E9) else Color(0xFFF0F4F1)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(rutina.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Checkbox(
                                checked = completada,
                                onCheckedChange = { completada = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF00684A))
                            )
                        }

                        Text("Nivel: ${rutina.nivel}", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))

                        rutina.ejercicios.forEach { ejercicio ->
                            Text("• $ejercicio", fontSize = 13.sp, color = Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}
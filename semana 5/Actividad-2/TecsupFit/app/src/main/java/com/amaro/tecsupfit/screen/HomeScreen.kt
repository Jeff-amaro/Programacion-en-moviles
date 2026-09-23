package com.amaro.tecsupfit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaro.tecsupfit.MockData

@Composable
fun HomeScreen(onClaseClick: (String) -> Unit) {
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Encabezado Verde
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF00684A))
                .padding(20.dp)
        ) {
            Column {
                Text("TECSUP Fit", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Hola, Diego", color = Color(0xFFC8E6C9), fontSize = 14.sp)
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // LazyRow para Chips de Filtro
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val opciones = listOf("Hoy", "Esta semana")
                items(opciones) { opcion ->
                    FilterChip(
                        selected = filtroSeleccionado == opcion,
                        onClick = { filtroSeleccionado = opcion },
                        label = { Text(opcion) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF00684A),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Clases disponibles", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // LazyColumn para Lista de Clases
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val listaFiltrada = if (filtroSeleccionado == "Hoy") {
                    MockData.clasesDisponibles.filter { it.esHoy }
                } else {
                    MockData.clasesDisponibles
                }

                items(listaFiltrada) { clase ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClaseClick(clase.id) },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F1)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFF00684A),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(clase.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("${clase.horario} - ${clase.sala}", color = Color.Gray, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
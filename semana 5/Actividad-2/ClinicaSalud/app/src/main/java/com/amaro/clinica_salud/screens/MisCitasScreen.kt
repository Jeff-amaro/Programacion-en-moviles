package com.amaro.clinica_salud.screens

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
import com.amaro.clinica_salud.clinica.Cita

@Composable
fun MisCitasScreen(
    listaCitas: MutableList<Cita>
) {
    // [ETIQUETA: ESTADO DEL DIÁLOGO] - Almacena la cita seleccionada para cancelar (null = diálogo oculto)
    var citaACancelar by remember { mutableStateOf<Cita?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis citas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (listaCitas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tienes citas agendadas.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listaCitas) { cita ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF6F0F8)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            // [ETIQUETA: BARRA DE ESTADO LATERAL] - Línea vertical morada si la cita está confirmada
                            Surface(
                                modifier = Modifier
                                    .width(6.dp)
                                    .fillMaxHeight(),
                                color = if (cita.estado == "Confirmada") Color(0xFF4A148C) else Color.Transparent
                            ) {}

                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = cita.doctorNombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${cita.fecha}, ${cita.hora}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // [ETIQUETA: INSIGNIA DE ESTADO] - Chip verde para 'Confirmada' y gris para 'Completada'
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (cita.estado == "Confirmada") Color(0xFFE8F5E9) else Color(0xFFEEEEEE)
                                    ) {
                                        Text(
                                            text = cita.estado,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (cita.estado == "Confirmada") Color(0xFF2E7D32) else Color.DarkGray
                                        )
                                    }

                                    // [ETIQUETA: BOTÓN CANCELAR] - Solo visible para citas confirmadas
                                    if (cita.estado == "Confirmada") {
                                        TextButton(onClick = { citaACancelar = cita }) {
                                            Text(
                                                text = "Cancelar",
                                                color = MaterialTheme.colorScheme.error,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // [ETIQUETA: DIÁLOGOS DE CONFIRMACIÓN ALERTDIALOG] - Se activa dinámicamente cuando citaACancelar != null
    citaACancelar?.let { cita ->
        AlertDialog(
            onDismissRequest = { citaACancelar = null },
            title = { Text("Confirmar cancelación") },
            text = { Text("¿Estás seguro de que deseas cancelar la cita con ${cita.doctorNombre} para el ${cita.fecha}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // [ETIQUETA: ELIMINACIÓN REACTIVA] - Remueve la cita y refresca la interfaz automáticamente
                        listaCitas.remove(cita)
                        citaACancelar = null
                    }
                ) {
                    Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { citaACancelar = null }) {
                    Text("No")
                }
            }
        )
    }
}
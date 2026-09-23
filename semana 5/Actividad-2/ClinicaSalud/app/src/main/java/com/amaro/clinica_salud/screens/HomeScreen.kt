package com.amaro.clinica_salud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amaro.clinica_salud.clinica.ClinicaData
import com.amaro.clinica_salud.clinica.Doctor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var especialidadSeleccionada by remember { mutableStateOf("Todas") }

    val doctoresFiltrados = if (especialidadSeleccionada == "Todas") {
        ClinicaData.doctores
    } else {
        ClinicaData.doctores.filter { it.especialidad == especialidadSeleccionada }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Especialidades",
            style = MaterialTheme.typography.titleMedium
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(ClinicaData.especialidades) { especialidad ->
                FilterChip(
                    selected = (especialidad == especialidadSeleccionada),
                    onClick = { especialidadSeleccionada = especialidad },
                    label = { Text(especialidad) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Médicos Disponibles",
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            items(doctoresFiltrados) { doctor ->
                DoctorCard(
                    doctor = doctor,
                    onVerDetalle = { navController.navigate("detalle/${doctor.id}") }
                )
            }
        }
    }
}

@Composable
fun DoctorCard(
    doctor: Doctor,
    onVerDetalle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = doctor.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = doctor.especialidad,
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${doctor.calificacion}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = onVerDetalle,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            ) {
                Text("Ver Perfil")
            }
        }
    }
}
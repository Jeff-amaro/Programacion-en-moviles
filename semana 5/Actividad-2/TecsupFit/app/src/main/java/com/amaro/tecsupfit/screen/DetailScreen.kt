package com.amaro.tecsupfit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaro.tecsupfit.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    claseId: String,
    onBackClick: () -> Unit,
    onReservarClick: (String, String) -> Unit
) {
    val clase = MockData.clasesDisponibles.find { it.id == claseId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (clase != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Hero Icon Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = Color(0xFF00684A),
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(clase.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("${clase.horario} · ${clase.sala} · 45 min", color = Color.Gray, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(clase.descripcion, fontSize = 14.sp, color = Color.DarkGray)

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "${clase.cuposDisponibles} de ${clase.cuposTotales} cupos disponibles",
                        color = Color(0xFF00684A),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Button(
                    onClick = {
                        MockData.agregarReserva(clase.nombre, clase.horario)
                        onReservarClick(clase.nombre, clase.horario)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00684A)),
                    shape = RoundedCornerShape(25.dp),
                    enabled = clase.cuposDisponibles > 0
                ) {
                    Text(
                        if (clase.cuposDisponibles > 0) "Reservar cupo" else "Cupos agotados",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
package com.amaro.tecsupfit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaro.tecsupfit.MockData

@Composable
fun ReservasScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mis reservas", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(MockData.misReservasIniciales) { reserva ->
                val esConfirmada = reserva.estado == "Confirmada"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F1)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Borde o barra verde de acento a la izquierda si está confirmada
                        if (esConfirmada) {
                            Box(
                                modifier = Modifier
                                    .width(6.dp)
                                    .height(80.dp)
                                    .background(Color(0xFF00684A))
                            )
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(reserva.claseNombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(reserva.horario, color = Color.Gray, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                color = if (esConfirmada) Color(0xFFE8F5E9) else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = reserva.estado,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = if (esConfirmada) Color(0xFF00684A) else Color.DarkGray,
                                    fontSize = 12.sp,
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
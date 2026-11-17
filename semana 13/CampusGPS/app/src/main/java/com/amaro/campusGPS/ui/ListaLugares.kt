
package com.amaro.campusGPS.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaro.campusGPS.model.LugarConDistancia
import com.amaro.campusGPS.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    idMasCercano: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(
            items = lugares,
            key = { it.lugar.id }
        ) { item ->

            val lugar = item.lugar
            val estaSeleccionado = seleccionado == lugar.id

            Surface(
                onClick = { onClick(lugar.id) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(9.dp),
                color = if (estaSeleccionado) {
                    Color(0xFFE2F1FB)
                } else {
                    Color(0xFFF5F7F9)
                },
                border = BorderStroke(
                    width = 1.dp,
                    color = if (estaSeleccionado) {
                        Color(0xFF12699C)
                    } else {
                        Color(0xFFE0E5E9)
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 9.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .background(
                                if (lugar.id == idMasCercano) {
                                    Color(0xFF2E9B45)
                                } else {
                                    Color(0xFF2196F3)
                                },
                                CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(9.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = lugar.nombre,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 17.sp
                        )

                        Text(
                            text = lugar.categoria,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.Gray,
                            lineHeight = 14.sp
                        )
                    }

                    Text(
                        text = item.metros?.let {
                            formatearDistancia(it)
                        } ?: "Sin calcular",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF12699C)
                    )
                }
            }
        }
    }
}

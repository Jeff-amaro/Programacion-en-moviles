
package com.amaro.campusGPS.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaro.campusGPS.model.TipoError
import com.amaro.campusGPS.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit
) {
    val azulCampus = Color(0xFF12699C)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFF9DEDC),
            shape = RoundedCornerShape(9.dp)
        ) {
            Text(
                text = error.mensaje,
                modifier = Modifier.padding(13.dp),
                color = Color(0xFFB3261E),
                fontFamily = FontFamily.SansSerif,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (error.tipo) {

            TipoError.SIN_PERMISO -> {
                OutlinedButton(
                    onClick = onAbrirAjustes,
                    border = BorderStroke(
                        width = 2.dp,
                        color = azulCampus
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = azulCampus
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Abrir ajustes",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            TipoError.GPS_APAGADO -> {
                OutlinedButton(
                    onClick = onReintentar,
                    border = BorderStroke(
                        width = 2.dp,
                        color = azulCampus
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = azulCampus
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Reintentar",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            TipoError.SIN_PLAY_SERVICES,
            TipoError.OTRO -> Unit
        }
    }
}

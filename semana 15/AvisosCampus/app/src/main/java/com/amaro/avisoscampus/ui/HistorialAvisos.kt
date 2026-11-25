package com.amaro.avisoscampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amaro.avisoscampus.model.Aviso

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Historial",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (avisos.isEmpty()) {
                Text("Sin avisos todavía")
            } else {
                avisos.take(20).forEach { aviso ->

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = aviso.origen.texto,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium
                        )

                        Text(
                            text = aviso.hora,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Text(
                        text = aviso.titulo,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        text = aviso.texto,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

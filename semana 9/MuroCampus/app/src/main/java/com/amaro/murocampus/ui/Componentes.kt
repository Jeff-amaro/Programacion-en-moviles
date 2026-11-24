package com.amaro.murocampus.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun TarjetaPost(
    userId: Int,
    title: String,
    body: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Usuario $userId",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = body,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun Cargando(
    mensaje: String = "Cargando publicaciones..."
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = mensaje)
        }
    }
}

@Composable
fun ErrorConReintento(
    mensaje: String,
    onReintentar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onReintentar) {
                    Text("Reintentar")
                }
            }
        }
    }
}

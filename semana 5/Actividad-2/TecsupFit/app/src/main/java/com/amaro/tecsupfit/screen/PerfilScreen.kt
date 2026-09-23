package com.amaro.tecsupfit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
fun PerfilScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Mi perfil", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Avatar con iniciales JA
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFE8F5E9), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("JA", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00684A))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Jeff Amaro", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Plan Premium", fontSize = 13.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(30.dp))

        // Estadísticas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(titulo = "${MockData.clasesCompletadas}", subtitulo = "Clases", modifier = Modifier.weight(1f))
            StatCard(titulo = "${MockData.rachaDias}", subtitulo = "Rachas", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun StatCard(titulo: String, subtitulo: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F1)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(subtitulo, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
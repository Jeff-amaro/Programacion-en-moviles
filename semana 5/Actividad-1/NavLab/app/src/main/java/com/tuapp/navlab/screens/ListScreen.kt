package com.tuapp.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.tuapp.navlab.navigation.Screen

data class Student(
    val id: Int,
    val name: String,
    val career: String,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    val studentList = listOf(
        Student(1, "Jeff Amaro", "Ingeniería de Software", "https://i.pravatar.cc/150?img=11"),
        Student(2, "María García", "Arquitectura", "https://i.pravatar.cc/150?img=5"),
        Student(3, "Carlos Pérez", "Medicina", "https://i.pravatar.cc/150?img=12"),
        Student(4, "Ana López", "Derecho", "https://i.pravatar.cc/150?img=9"),
        Student(5, "Luís Ramírez", "Administración", "https://i.pravatar.cc/150?img=60")
    )

    // Degradado vertical de lavanda intenso a violeta claro tenue
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE8DEF8), // Morado lavanda superior (Barra de estado / TopBar)
            Color(0xFFF3EDF7), // Degradado intermedio
            Color(0xFFFBF8FD), // Tono muy tenue abajo
            Color(0xFFFFFFFF)  // Blanco puro en la base
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Directorio de Alumnos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF381E72)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Atrás",
                                tint = Color(0xFF381E72)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent // Para que traspase el degradado de fondo
                    )
                )
            },
            containerColor = Color.Transparent // Permite ver el degradado
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(studentList) { student ->
                    StudentCard(
                        student = student,
                        onClick = {
                            val route = Screen.Detail.route.replace("{itemId}", student.id.toString())
                            navController.navigate(route)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StudentCard(student: Student, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3EDF7).copy(alpha = 0.90f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = student.imageUrl,
                contentDescription = student.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1D1B20)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = student.career,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF6750A4)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF79747E),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
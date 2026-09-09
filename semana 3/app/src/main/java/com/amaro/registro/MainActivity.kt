package com.amaro.registro

import android.os.Bundle
import java.util.Locale
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                RegistroNotasScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroNotasScreen(){
    var notaFundamentos by remember {
        mutableFloatStateOf(0f)
    }
    var notaPoo by remember {
        mutableFloatStateOf(0f)
    }
    var notaMoviles by remember {
        mutableFloatStateOf(0f)
    }
    var notaBd by remember {
        mutableFloatStateOf(0f)
    }
    var redondear by remember {
        mutableStateOf(false)
    }
    var confirmado by remember {
        mutableStateOf(false)
    }
    var calculado by remember {
        mutableStateOf(false)
    }
    val purplePrimary = Color(0xFF6750A4)
    val purpleBackground = Color(0xFFEADDFF)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Registro de Notas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = purplePrimary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(purpleBackground, Color.White)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ingreso de Notas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = purplePrimary,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )

                CursoSliderRow(
                    nombreCurso = "Fundamentos de Programación",
                    porcentaje = "20%",
                    nota = notaFundamentos,
                    onNotaChange = {
                        notaFundamentos = it
                        calculado = false
                    }
                )

                CursoSliderRow(
                    nombreCurso = "Programacion Orientada a Objetos",
                    porcentaje = "25%",
                    nota = notaPoo,
                    onNotaChange = {
                        notaPoo = it
                        calculado = false
                    }
                )

                CursoSliderRow(
                    nombreCurso = "Programacion en Moviles",
                    porcentaje = "30%",
                    nota = notaMoviles,
                    onNotaChange = {
                        notaMoviles = it
                        calculado = false
                    }
                )

                CursoSliderRow(
                    nombreCurso = "Base de Datos",
                    porcentaje = "20%",
                    nota = notaBd,
                    onNotaChange = {
                        notaBd = it
                        calculado = false
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Redondear nota promedio",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Switch(
                                checked = redondear,
                                onCheckedChange = { redondear = it }
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = confirmado,
                                onCheckedChange = { confirmado = it }
                            )
                            Text(
                                text = "Confirmo que las notas son correctas",
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { calculado = true },
                    enabled = confirmado,
                    colors = ButtonDefaults.buttonColors(containerColor = purplePrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Calcular Promedio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
@Composable
fun CursoSliderRow(
    nombreCurso: String,
    porcentaje: String,
    nota: Float,
    onNotaChange: (Float) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = nombreCurso,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1D1B20)
                )
                Badge(
                    containerColor = Color(0xFF6750A4),
                    contentColor = Color.White
                ) {
                    Text(
                        text = porcentaje,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(
                    value = nota,
                    onValueChange = onNotaChange,
                    valueRange = 0f..20f,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = String.format(Locale.US, "%.1f", nota),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF6750A4)
                )
            }
        }
    }
}



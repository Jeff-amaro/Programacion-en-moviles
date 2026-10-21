package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    viewModel: SesionViewModel,
    onLoginExitoso: (String) -> Unit,
    onIrARegistro: () -> Unit
) {
    var rolSeleccionado by remember { mutableStateOf("CLIENTE") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    val error by viewModel.error.collectAsState()

    val esEstudiante = rolSeleccionado == "CLIENTE"
    val colorTema = if (esEstudiante) Color(0xFFE65100) else Color(0xFF4F46E5)
    val colorFondoCardAdmin = Color(0xFFEEF2FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Bienvenido",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Text(
            text = "Ingresa para continuar",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F4F6), RoundedCornerShape(16.dp))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (esEstudiante) Color(0xFFE65100) else Color.Transparent,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        rolSeleccionado = "CLIENTE"
                        viewModel.limpiarError()
                    }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Estudiante",
                    color = if (esEstudiante) Color.White else Color(0xFF6B7280),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (!esEstudiante) Color(0xFF4F46E5) else Color.Transparent,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        rolSeleccionado = "ADMIN"
                        viewModel.limpiarError()
                    }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Administrador",
                    color = if (!esEstudiante) Color.White else Color(0xFF6B7280),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                viewModel.limpiarError()
            },
            label = { Text("Correo") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Correo",
                    tint = colorTema
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorTema,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedLabelColor = colorTema,
                unfocusedLabelColor = Color(0xFF9CA3AF)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = {
                clave = it
                viewModel.limpiarError()
            },
            label = { Text("Contraseña") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Contraseña",
                    tint = colorTema
                )
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorTema,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedLabelColor = colorTema,
                unfocusedLabelColor = Color(0xFF9CA3AF)
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = colorTema,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { }
            )
        }

        error?.let { mensaje ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                viewModel.login(correo, clave, rolSeleccionado) { rol ->
                    onLoginExitoso(rol)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorTema),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Ingresar",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        if (esEstudiante) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("¿No tienes cuenta? ")
                        withStyle(style = SpanStyle(color = Color(0xFFE65100), fontWeight = FontWeight.Bold)) {
                            append("Regístrate")
                        }
                    },
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.clickable { onIrARegistro() }
                )
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorFondoCardAdmin)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Modo práctica",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F46E5)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}
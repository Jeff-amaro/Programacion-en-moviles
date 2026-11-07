package com.tecsup.productos.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.productos.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginExito: (com.tecsup.productos.domain.model.Rol) -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var showErrorCorreo by remember { mutableStateOf(false) }
    var showErrorClave by remember { mutableStateOf(false) }

    val inputShape = RoundedCornerShape(16.dp)

    LaunchedEffect(estado) {
        if (estado is LoginEstado.Exito) {
            onLoginExito((estado as LoginEstado.Exito).sesion.rol)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.verificarSesion()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo de la app",
            modifier = Modifier
                .size(125.dp)
                .clip(RoundedCornerShape(28.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Inicia sesión para continuar",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp, top = 8.dp)
        )

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                if (it.isNotEmpty()) showErrorCorreo = false
            },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Ícono de correo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showErrorCorreo,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = inputShape,
            supportingText = {
                if (showErrorCorreo) {
                    Text("El correo es obligatorio", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = {
                clave = it
                if (it.isNotEmpty()) showErrorClave = false
            },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Ícono de candado") },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, "Alternar visibilidad de contraseña")
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showErrorClave,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = inputShape,
            supportingText = {
                if (showErrorClave) {
                    Text("La contraseña es obligatoria", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (estado is LoginEstado.Error) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(
                    text = (estado as LoginEstado.Error).mensaje,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = {
                showErrorCorreo = correo.isBlank()
                showErrorClave = clave.isBlank()

                if (!showErrorCorreo && !showErrorClave) {
                    viewModel.login(correo, clave)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = estado !is LoginEstado.Cargando,
            shape = RoundedCornerShape(16.dp)
        ) {
            if (estado is LoginEstado.Cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Ingresar", fontSize = MaterialTheme.typography.titleMedium.fontSize)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
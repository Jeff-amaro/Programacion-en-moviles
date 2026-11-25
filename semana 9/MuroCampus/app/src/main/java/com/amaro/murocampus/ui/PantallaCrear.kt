
package com.amaro.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amaro.murocampus.viewmodel.PostsViewModel
import com.amaro.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(
    vm: PostsViewModel,
    modifier: Modifier = Modifier
) {
    var titulo by rememberSaveable {
        mutableStateOf("")
    }

    var contenido by rememberSaveable {
        mutableStateOf("")
    }

    var formularioLimpiado by rememberSaveable {
        mutableStateOf(false)
    }

    val estado by vm.publicacion.collectAsStateWithLifecycle()

    val cargando = estado == UiState.Cargando

    LaunchedEffect(estado) {
        if (estado is UiState.Exito && !formularioLimpiado) {
            titulo = ""
            contenido = ""
            formularioLimpiado = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !cargando
        )

        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            enabled = !cargando
        )

        Button(
            onClick = {
                formularioLimpiado = false
                vm.publicar(titulo, contenido)
            },
            enabled = titulo.isNotBlank() &&
                    contenido.isNotBlank() &&
                    !cargando,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1565A5),
                contentColor = Color.White
            )
        ) {
            Text("Publicar")
        }

        when (val actual = estado) {

            null -> Unit

            UiState.Cargando -> {
                CircularProgressIndicator(
                    color = Color(0xFF1565A5)
                )

                Text("Publicando...")
            }

            is UiState.Exito -> {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFDDF5E3)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Publicado con id ${actual.datos.id}",
                            color = Color(0xFF216E39),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            is UiState.Error -> {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFDAD6)
                    )
                ) {
                    Text(
                        text = actual.mensaje,
                        color = Color(0xFF690005),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

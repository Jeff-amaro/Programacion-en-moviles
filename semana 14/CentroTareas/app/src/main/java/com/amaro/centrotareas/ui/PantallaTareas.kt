package com.amaro.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.amaro.centrotareas.model.CargaUi
import com.amaro.centrotareas.model.PrimosUi
import com.amaro.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {

    val mensaje by viewModel.mensaje.collectAsState()
    val primos by viewModel.primos.collectAsState()
    val carga by viewModel.carga.collectAsState()

    var toques by rememberSaveable {
        mutableIntStateOf(0)
    }

    var hasta by rememberSaveable {
        mutableStateOf("500000")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("CentroTareas")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary,
                    titleContentColor =
                        MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // TARJETA 1: TAREA CON PROGRESO

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        "Tarea con progreso",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Button(
                        onClick = { toques++ }
                    ) {
                        Text("Tócame: $toques")
                    }

                    Button(
                        onClick = {
                            viewModel.tareaLarga()
                        },
                        enabled = true
                    ) {
                        Text("Tarea larga")
                    }


                    Text(mensaje)

                    // TODO Paso 3: barra de progreso
                    // TODO Paso 4: botón Cancelar
                }
            }

            // TARJETA 2: CONTAR PRIMOS

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        "Contar números primos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = hasta,
                        onValueChange = { hasta = it },
                        label = { Text("Hasta qué número") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                // TODO Paso 5
                            },
                            enabled = false
                        ) {
                            Text("Contar")
                        }

                        OutlinedButton(
                            onClick = {
                                // TODO Paso 5
                            },
                            enabled = false
                        ) {
                            Text("Cancelar")
                        }
                    }

                    when (val estado = primos) {
                        PrimosUi.Inicial ->
                            Text("Esperando cálculo")

                        PrimosUi.Calculando ->
                            CircularProgressIndicator()

                        is PrimosUi.Listo ->
                            Text(
                                "Primos: ${estado.cantidad}\n" +
                                        "Tiempo: ${estado.milisegundos} ms"
                            )

                        PrimosUi.Cancelado ->
                            Text("Cancelado")
                    }
                }
            }

            // TARJETA 3: DOS CARGAS

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        "Dos cargas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                // TODO Paso 6
                            },
                            enabled = false
                        ) {
                            Text("Una por una")
                        }

                        Button(
                            onClick = {
                                // TODO Paso 6
                            },
                            enabled = false
                        ) {
                            Text("A la vez")
                        }
                    }

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Simular error")

                        Switch(
                            checked = false,
                            onCheckedChange = null,
                            enabled = false
                        )
                    }

                    when (val estado = carga) {
                        CargaUi.Inicial ->
                            Text("Esperando carga")

                        CargaUi.Cargando ->
                            CircularProgressIndicator()

                        is CargaUi.Exito ->
                            Text(
                                "Modo: ${estado.modo}\n" +
                                        "Perfil: ${estado.perfil}\n" +
                                        "Pedidos: ${estado.pedidos}\n" +
                                        "Tiempo: ${estado.milisegundos} ms"
                            )

                        is CargaUi.Error ->
                            Text("Error: ${estado.mensaje}")
                    }

                    // TODO Paso 7: botón Reintentar
                }
            }
        }
    }
}

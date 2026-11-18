
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
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {

    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()

    var toques by rememberSaveable {
        mutableIntStateOf(0)
    }

    var hasta by rememberSaveable {
        mutableStateOf("500000")
    }
    val numero = hasta.toIntOrNull()

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

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        Button(
                            onClick = {
                                viewModel.tareaLarga()
                            },
                            enabled = !corriendo
                        ) {
                            Text("Iniciar")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.cancelar()
                            },
                            enabled = corriendo
                        ) {
                            Text("Cancelar")
                        }
                    }

                    Text(mensaje)

                    // PASO 3: Barra de progreso

                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "${(progreso * 100).toInt()} %"
                    )
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
                        label = {
                            Text("Hasta qué número")
                        },
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
                                numero?.takeIf { it >= 2 }?.let {
                                    viewModel.contar(it)
                                }
                            },
                            enabled = numero != null &&
                                    numero >= 2 &&
                                    primos != PrimosUi.Calculando
                        ) {
                            Text("Contar")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.cancelarPrimos()
                            },
                            enabled = primos == PrimosUi.Calculando
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
                            Surface(
                                color = Color(0xFFE6F4EA),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(
                                    text = "Primos: ${estado.cantidad}\n" +
                                            "Tiempo: ${estado.milisegundos} ms",
                                    modifier = Modifier.padding(12.dp),
                                    color = Color(0xFF1B5E20)
                                )
                            }

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
                                viewModel.cargarUnaPorUna()
                            },
                            enabled = carga != CargaUi.Cargando
                        ) {
                            Text("Una por una")
                        }

                        Button(
                            onClick = {
                                viewModel.cargarALaVez()
                            },
                            enabled = carga != CargaUi.Cargando
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
                            checked = simularError,
                            onCheckedChange = {
                                viewModel.cambiarSimularError(it)
                            }
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

                        is CargaUi.Error -> {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {

                                    Text(
                                        text = estado.mensaje,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )

                                    Button(
                                        onClick = {
                                            viewModel.cargarALaVez()
                                        }
                                    ) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import java.text.DateFormat
import java.util.Date
import com.amaro.centrotareas.model.CargaUi
import com.amaro.centrotareas.model.PrimosUi
import com.amaro.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {

    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    val progresoPrimos by viewModel.progresoPrimos.collectAsState()
    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()

    val limiteDosSegundos by
    viewModel.limiteDosSegundos.collectAsState()

    // MEJORA 5: Historial de resultados

    val historial by viewModel.historial.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.iniciarHistorial(
            context.applicationContext
        )
    }

    // MEJORA 6: Comparación de tiempos

    val tiempos by viewModel.tiempos.collectAsState()

    val ultimoSecuencial = tiempos.firstOrNull {
        it.modo == "Una por una"
    }?.milisegundos

    val ultimoParalelo = tiempos.firstOrNull {
        it.modo == "A la vez"
    }?.milisegundos

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
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
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
                        style =
                            MaterialTheme.typography.titleMedium,
                        color =
                            MaterialTheme.colorScheme.primary
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
                        style =
                            MaterialTheme.typography.titleMedium,
                        color =
                            MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = hasta,
                        onValueChange = {
                            hasta = it
                        },
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

                                numero
                                    ?.takeIf { it >= 2 }
                                    ?.let {
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
                            enabled =
                                primos == PrimosUi.Calculando
                        ) {
                            Text("Cancelar")
                        }
                    }

                    when (val estado = primos) {

                        PrimosUi.Inicial ->
                            Text("Esperando cálculo")

                        PrimosUi.Calculando ->

                            Column(
                                verticalArrangement =
                                    Arrangement.spacedBy(8.dp)
                            ) {

                                LinearProgressIndicator(
                                    progress = { progresoPrimos },
                                    modifier =
                                        Modifier.fillMaxWidth()
                                )

                                Text(
                                    "Calculando: " +
                                            "${(progresoPrimos * 100).toInt()} %"
                                )
                            }

                        is PrimosUi.Listo ->

                            Surface(
                                color = Color(0xFFE6F4EA),
                                shape =
                                    MaterialTheme.shapes.medium
                            ) {

                                Text(
                                    text =
                                        "Primos: ${estado.cantidad}\n" +
                                                "Tiempo: ${estado.milisegundos} ms",
                                    modifier =
                                        Modifier.padding(12.dp),
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
                        style =
                            MaterialTheme.typography.titleMedium,
                        color =
                            MaterialTheme.colorScheme.primary
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        OutlinedButton(
                            onClick = {
                                viewModel.cargarUnaPorUna()
                            },
                            enabled =
                                carga != CargaUi.Cargando
                        ) {
                            Text("Una por una")
                        }

                        Button(
                            onClick = {
                                viewModel.cargarALaVez()
                            },
                            enabled =
                                carga != CargaUi.Cargando
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

                    // MEJORA 2: Tiempo máximo

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        Text("Límite de 2 segundos")

                        Switch(
                            checked = limiteDosSegundos,
                            onCheckedChange = {
                                viewModel.cambiarLimiteDosSegundos(it)
                            },
                            enabled =
                                carga != CargaUi.Cargando
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
                                color =
                                    MaterialTheme.colorScheme.errorContainer,
                                shape =
                                    MaterialTheme.shapes.medium
                            ) {

                                Column(
                                    modifier =
                                        Modifier.padding(12.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {

                                    Text(
                                        text = estado.mensaje,
                                        color =
                                            MaterialTheme.colorScheme.onErrorContainer
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

            // TARJETA 4: HISTORIAL DE RESULTADOS

            // MEJORA 5: Guardar los últimos 5 resultados

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        "Historial de resultados",
                        style =
                            MaterialTheme.typography.titleMedium,
                        color =
                            MaterialTheme.colorScheme.primary
                    )

                    if (historial.isEmpty()) {

                        Text(
                            "Todavía no hay resultados guardados"
                        )

                    } else {

                        historial.forEachIndexed {
                                indice, registro ->

                            if (indice > 0) {
                                HorizontalDivider()
                            }

                            Text(
                                text =
                                    "${indice + 1}. ${registro.tipo}",
                                style =
                                    MaterialTheme.typography.titleSmall
                            )

                            Text(
                                text = registro.detalle
                            )

                            Text(
                                text =
                                    DateFormat.getDateTimeInstance(
                                        DateFormat.SHORT,
                                        DateFormat.SHORT
                                    ).format(
                                        Date(registro.fechaMillis)
                                    ),
                                style =
                                    MaterialTheme.typography.bodySmall,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // TARJETA 5: COMPARACION DE TIEMPOS

            // MEJORA 6: Tabla de cargas secuenciales y paralelas

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        "Comparación de tiempos",
                        style =
                            MaterialTheme.typography.titleMedium,
                        color =
                            MaterialTheme.colorScheme.primary
                    )

                    Text(
                        "Registro de las últimas 10 cargas exitosas.",
                        style =
                            MaterialTheme.typography.bodySmall
                    )

                    if (tiempos.isEmpty()) {

                        Text(
                            "Todavía no hay tiempos registrados. " +
                                    "Ejecuta Una por una y A la vez."
                        )

                    } else {

                        // Cabecera de la tabla

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(
                                "N.º",
                                modifier =
                                    Modifier.weight(0.5f),
                                style =
                                    MaterialTheme.typography.labelMedium
                            )

                            Text(
                                "Modo",
                                modifier =
                                    Modifier.weight(1.5f),
                                style =
                                    MaterialTheme.typography.labelMedium
                            )

                            Text(
                                "Tiempo",
                                modifier =
                                    Modifier.weight(1f),
                                style =
                                    MaterialTheme.typography.labelMedium
                            )
                        }

                        HorizontalDivider()

                        // Filas con los tiempos registrados

                        tiempos.forEachIndexed {
                                indice, registro ->

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth()
                            ) {

                                Text(
                                    "${indice + 1}",
                                    modifier =
                                        Modifier.weight(0.5f)
                                )

                                Text(
                                    registro.modo,
                                    modifier =
                                        Modifier.weight(1.5f)
                                )

                                Text(
                                    "${registro.milisegundos} ms",
                                    modifier =
                                        Modifier.weight(1f)
                                )
                            }

                            HorizontalDivider()
                        }
                    }

                    // Comparar el último tiempo de cada modo

                    if (
                        ultimoSecuencial != null &&
                        ultimoParalelo != null
                    ) {

                        val diferencia =
                            ultimoSecuencial - ultimoParalelo

                        val ahorroPorcentaje =
                            if (ultimoSecuencial > 0L) {
                                diferencia * 100.0 /
                                        ultimoSecuencial
                            } else {
                                0.0
                            }

                        Surface(
                            color =
                                MaterialTheme.colorScheme.primaryContainer,
                            shape =
                                MaterialTheme.shapes.medium
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(12.dp),
                                verticalArrangement =
                                    Arrangement.spacedBy(6.dp)
                            ) {

                                Text(
                                    "Última comparación",
                                    style =
                                        MaterialTheme.typography.titleSmall
                                )

                                Text(
                                    "Una por una: " +
                                            "$ultimoSecuencial ms"
                                )

                                Text(
                                    "A la vez: " +
                                            "$ultimoParalelo ms"
                                )

                                if (diferencia > 0) {

                                    Text(
                                        "Ahorro: $diferencia ms " +
                                                "(${String.format("%.1f", ahorroPorcentaje)} %)"
                                    )

                                } else if (diferencia < 0) {

                                    Text(
                                        "En esta prueba, A la vez " +
                                                "tardó ${-diferencia} ms más."
                                    )

                                } else {

                                    Text(
                                        "Ambos métodos tardaron lo mismo."
                                    )
                                }
                            }
                        }

                    } else {

                        Text(
                            "Realiza al menos una carga de cada " +
                                    "modo para ver la comparación.",
                            style =
                                MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

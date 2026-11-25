package com.amaro.avisoscampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.amaro.avisoscampus.notificacion.Notificador
import com.amaro.avisoscampus.viewmodel.AvisosViewModel
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
import com.amaro.avisoscampus.model.PermisoUi
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.amaro.avisoscampus.receiver.ModoAvionReceiver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {

    val contexto = LocalContext.current

    val permiso by vm.permiso.collectAsState()

    val faltan by vm.faltan.collectAsState()

    val escuchando by vm.escuchandoAvion.collectAsState()

    val avisos by vm.avisos.collectAsState()

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }
    }

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: manejar permiso denegado
        }
    }

    DisposableEffect(escuchando) {
        if (escuchando) {
            val receptor = ModoAvionReceiver { activo ->
                vm.alCambiarModoAvion(activo)
            }

            ContextCompat.registerReceiver(
                contexto,
                receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )

            onDispose {
                contexto.unregisterReceiver(receptor)
            }
        } else {
            onDispose { }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            TarjetaAvisos("Permiso de notificaciones") {
                val estado = when (permiso) {
                    PermisoUi.SinPedir -> "Sin pedir"
                    PermisoUi.Concedido -> "Permitido"
                    PermisoUi.Denegado -> "Denegado"
                }

                val colorEstado = when (permiso) {
                    PermisoUi.SinPedir -> Color.Gray
                    PermisoUi.Concedido -> Color(0xFF2E7D32)
                    PermisoUi.Denegado -> Color.Red
                }

                Text(
                    text = estado,
                    color = colorEstado
                )

                Button(onClick = {
                    // TODO Paso 2
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lanzador.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    } else {
                        vm.permisoRespondido(true)
                    }
                }) {
                    Text("Permitir notificaciones")
                }
            }

            TarjetaAvisos("Avisos") {
                Button(onClick = {
                    // TODO Paso 3
                    vm.avisarAhora()
                }) {
                    Text("Mostrar aviso")
                }
            }

            TarjetaAvisos("Recordatorio") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            // TODO Paso 5
                            vm.recordarEn(10)
                        },
                        enabled = faltan == null
                    ) {
                        Text("Recordar en 10 s")
                    }

                    OutlinedButton(
                        onClick = {
                            vm.cancelarRecordatorio()
                        },
                        enabled = faltan != null
                    ) {
                        Text("Cancelar")
                    }
                }

                if (faltan != null) {
                    Text("Faltan $faltan s")
                }
            }

            TarjetaAvisos("Receptor de anuncios") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Escuchar el modo avión")

                    Switch(
                        checked = escuchando,
                        onCheckedChange = {
                            // TODO Paso 6
                            vm.cambiarEscucha(it)
                        }
                    )
                }
            }

            // TODO Paso 7
            HistorialAvisos(avisos = avisos)
        }
    }
}

@Composable
private fun TarjetaAvisos(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            contenido()
        }
    }
}

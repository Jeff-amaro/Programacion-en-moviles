
package com.amaro.campusGPS.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.amaro.campusGPS.model.UbicacionUi
import com.amaro.campusGPS.viewmodel.MapaViewModel

private val AzulCampus = Color(0xFF12699C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {

    val contexto = LocalContext.current
    val estado by vm.estado.collectAsState()

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->

        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    val alPulsar: () -> Unit = {

        val permisoFino = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val permisoAproximado = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (permisoFino || permisoAproximado) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CampusGPS",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulCampus
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = alPulsar,
                enabled = estado !is UbicacionUi.Buscando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulCampus
                )
            ) {
                Text("Obtener mi ubicación")
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (val actual = estado) {

                is UbicacionUi.Inicial -> {
                    Text(
                        text = "Presiona el botón para obtener tu ubicación",
                        textAlign = TextAlign.Center
                    )
                }

                is UbicacionUi.Buscando -> {
                    CircularProgressIndicator(
                        color = AzulCampus
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Buscando ubicación...")
                }

                is UbicacionUi.Lista -> {
                    Text(
                        text = "Latitud: ${actual.posicion.latitud}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "Longitud: ${actual.posicion.longitud}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                is UbicacionUi.Error -> {

                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = actual.mensaje,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (actual.mensaje.contains(
                            "permiso",
                            ignoreCase = true
                        )
                    ) {

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                ).apply {
                                    data = Uri.fromParts(
                                        "package",
                                        contexto.packageName,
                                        null
                                    )
                                }

                                contexto.startActivity(intent)
                            }
                        ) {
                            Text(
                                text = "Abrir ajustes",
                                color = AzulCampus
                            )
                        }
                    }
                }
            }
        }
    }
}

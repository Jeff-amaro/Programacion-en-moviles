
package com.amaro.campusGPS.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.amaro.campusGPS.model.UbicacionUi
import com.amaro.campusGPS.util.formatearDistancia
import com.amaro.campusGPS.viewmodel.MapaViewModel
import java.util.Locale

private val AzulCampus = Color(0xFF12699C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {

    val contexto = LocalContext.current

    val esHorizontal =
        LocalConfiguration.current.orientation ==
                Configuration.ORIENTATION_LANDSCAPE

    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()

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
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
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
                .then(
                    if (esHorizontal) {
                        Modifier.verticalScroll(
                            rememberScrollState()
                        )
                    } else {
                        Modifier
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (estado !is UbicacionUi.Error) {
                MapaUbicacion(
                    estado = estado,
                    lugares = lugares,
                    seleccionado = seleccionado,
                    masCercano = masCercano,
                    modifier = if (esHorizontal) {
                        Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = alPulsar,
                enabled = estado !is UbicacionUi.Buscando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulCampus
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Obtener mi ubicación",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            }

            if (estado !is UbicacionUi.Error) {

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = {
                        masCercano?.let { lugar ->
                            vm.seleccionar(lugar.id)
                        }
                    },
                    enabled = masCercano != null &&
                            estado is UbicacionUi.Lista,
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (
                            masCercano != null &&
                            estado is UbicacionUi.Lista
                        ) {
                            AzulCampus
                        } else {
                            Color.LightGray
                        }
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AzulCampus,
                        disabledContentColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Ir al más cercano",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (estado is UbicacionUi.Lista) {
                masCercano?.let { lugar ->

                    val metros = lugares.firstOrNull {
                        it.lugar.id == lugar.id
                    }?.metros

                    if (metros != null) {
                        Text(
                            text = "Más cercano: ${lugar.nombre} " +
                                    "(${formatearDistancia(metros)})",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(
                                horizontal = 8.dp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            when (val actual = estado) {

                is UbicacionUi.Inicial -> {
                    Text(
                        text = "Presiona el botón para obtener tu ubicación",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                is UbicacionUi.Buscando -> {
                    CircularProgressIndicator(
                        color = AzulCampus
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Buscando ubicación...",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp
                    )
                }

                is UbicacionUi.Lista -> {
                    Text(
                        text = String.format(
                            Locale.US,
                            "Latitud: %.4f",
                            actual.posicion.latitud
                        ),
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp
                    )

                    Text(
                        text = String.format(
                            Locale.US,
                            "Longitud: %.4f",
                            actual.posicion.longitud
                        ),
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp
                    )
                }

                is UbicacionUi.Error -> {
                    MensajeError(
                        error = actual,
                        onAbrirAjustes = {
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
                        },
                        onReintentar = alPulsar
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (estado !is UbicacionUi.Error) {
                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    idMasCercano = masCercano?.id,
                    onClick = { id ->
                        vm.seleccionar(id)
                    },
                    modifier = Modifier.height(220.dp)
                )
            } else {
                if (esHorizontal) {
                    Spacer(modifier = Modifier.height(16.dp))
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

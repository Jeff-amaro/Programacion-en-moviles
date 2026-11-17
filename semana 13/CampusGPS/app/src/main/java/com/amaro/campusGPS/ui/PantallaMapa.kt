package com.amaro.campusGPS.ui

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.amaro.campusGPS.model.UbicacionUi
import com.amaro.campusGPS.util.formatearDistancia
import com.amaro.campusGPS.viewmodel.MapaViewModel
import java.util.Locale

private val AzulCampus = Color(0xFF12699C)

private fun Context.obtenerActividad(): ComponentActivity? {
    var actual: Context = this

    while (actual is ContextWrapper) {
        if (actual is ComponentActivity) {
            return actual
        }

        val base = actual.baseContext

        if (base === actual) {
            break
        }

        actual = base
    }

    return actual as? ComponentActivity
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {

    val contexto = LocalContext.current
    val modoOscuro = isSystemInDarkTheme()
    val colores = MaterialTheme.colorScheme
    val colorPrincipal = colores.primary
    val colorRojoSeguimiento = if (modoOscuro) Color(0xFFFF8A80) else Color(0xFFC62828)
    val colorVerdeSeguimiento = if (modoOscuro) Color(0xFF85D5B2) else Color(0xFF238C59)
    val colorAdvertencia = if (modoOscuro) Color(0xFFFFC77D) else Color(0xFFB45309)

    val esHorizontal =
        LocalConfiguration.current.orientation ==
                Configuration.ORIENTATION_LANDSCAPE

    val estado by vm.estado.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()

    val radioMetros by vm.radioMetros.collectAsState()
    val lugaresEnRadio by vm.lugaresEnRadio.collectAsState()

    val ultimoLugar by vm.ultimoLugar.collectAsState()
    val seguimientoActivo by vm.seguimientoActivo.collectAsState()

    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val lugaresVisibles by vm.lugaresVisibles.collectAsState()

    val actividad = remember(contexto) {
        contexto.obtenerActividad()
    }

    DisposableEffect(actividad, vm) {

        val observador = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_STOP) {
                vm.detenerSeguimiento()
            }
        }

        actividad?.lifecycle?.addObserver(observador)

        onDispose {
            actividad?.lifecycle?.removeObserver(observador)
            vm.detenerSeguimiento()
        }
    }

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    val lanzadorSeguimiento = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            vm.iniciarSeguimiento()
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

    val alPulsarSeguimiento: () -> Unit = {

        if (seguimientoActivo) {

            vm.detenerSeguimiento()

        } else {

            val permisoFino = ContextCompat.checkSelfPermission(
                contexto,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val permisoAproximado = ContextCompat.checkSelfPermission(
                contexto,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (permisoFino || permisoAproximado) {
                vm.iniciarSeguimiento()
            } else {
                lanzadorSeguimiento.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    Scaffold(
        containerColor = colores.background,
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
                    containerColor = AzulCampus,
                    titleContentColor = Color.White
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
                    lugares = lugaresVisibles,
                    seleccionado = seleccionado,
                    masCercano = masCercano,
                    radioMetros = radioMetros,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = alPulsar,
                    enabled = estado !is UbicacionUi.Buscando &&
                            !seguimientoActivo,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulCampus,
                        contentColor = Color.White,
                        disabledContainerColor = colores.onSurface.copy(alpha = 0.12f),
                        disabledContentColor = colores.onSurface.copy(alpha = 0.38f)
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Obtener mi ubicación",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp,
                        textAlign = TextAlign.Center
                    )
                }

                OutlinedButton(
                    onClick = alPulsarSeguimiento,
                    enabled = seguimientoActivo ||
                            estado !is UbicacionUi.Buscando,
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (seguimientoActivo) {
                            colorRojoSeguimiento
                        } else {
                            colorPrincipal
                        }
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (seguimientoActivo) {
                            colorRojoSeguimiento
                        } else {
                            colorPrincipal
                        }
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (seguimientoActivo) {
                            "Detener seguimiento"
                        } else {
                            "Iniciar seguimiento"
                        },
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (seguimientoActivo) {

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "● Seguimiento GPS activo",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorVerdeSeguimiento
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
                            colorPrincipal
                        } else {
                            colores.outline.copy(alpha = 0.5f)
                        }
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorPrincipal,
                        disabledContentColor = colores.onSurface.copy(alpha = 0.38f)
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

            if (estado !is UbicacionUi.Error) {
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Categoría:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorPrincipal
                    )

                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item(key = "todas") {
                            FilterChip(
                                selected = categoriaSeleccionada == null,
                                onClick = { vm.seleccionarCategoria(null) },
                                label = { Text("Todas", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = colores.surface,
                                    labelColor = colores.onSurface,
                                    selectedContainerColor = colores.primaryContainer,
                                    selectedLabelColor = colores.onPrimaryContainer
                                )
                            )
                        }

                        items(vm.categorias, key = { it }) { categoria ->
                            FilterChip(
                                selected = categoriaSeleccionada == categoria,
                                onClick = { vm.seleccionarCategoria(categoria) },
                                label = { Text(categoria, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = colores.surface,
                                    labelColor = colores.onSurface,
                                    selectedContainerColor = colores.primaryContainer,
                                    selectedLabelColor = colores.onPrimaryContainer
                                )
                            )
                        }
                    }
                }
            }

            if (ultimoLugar != null) {

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    color = colores.surfaceVariant,
                    contentColor = colores.onSurfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Row(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Último lugar seleccionado",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = colores.onSurfaceVariant
                            )

                            Text(
                                text = ultimoLugar?.nombre.orEmpty(),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorPrincipal
                            )
                        }

                        TextButton(
                            onClick = {
                                vm.irAlUltimoLugar()
                            },
                            enabled = estado is UbicacionUi.Lista,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = colorPrincipal
                            )
                        ) {
                            Text(
                                text = "Ir al último lugar",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (estado is UbicacionUi.Lista) {

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Radio de búsqueda",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorPrincipal
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    listOf(100, 200, 500).forEach { metros ->

                        FilterChip(
                            selected = radioMetros == metros,
                            onClick = {
                                vm.cambiarRadio(metros)
                            },
                            label = {
                                Text(
                                    text = "$metros m",
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = colores.surface,
                                labelColor = colores.onSurface,
                                selectedContainerColor = colores.primaryContainer,
                                selectedLabelColor = colores.onPrimaryContainer
                            )
                        )
                    }
                }

                Text(
                    text = if (lugaresEnRadio.isEmpty()) {
                        "No hay lugares dentro de $radioMetros m"
                    } else {
                        "${lugaresEnRadio.size} de ${lugaresVisibles.size} " +
                                "lugares dentro de $radioMetros m"
                    },
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (lugaresEnRadio.isEmpty()) {
                        colorAdvertencia
                    } else {
                        colorPrincipal
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        horizontal = 8.dp
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (estado is UbicacionUi.Lista) {

                masCercano?.let { lugar ->

                    val metros = lugaresVisibles.firstOrNull {
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
                        color = colorPrincipal
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
                    lugares = lugaresVisibles,
                    seleccionado = seleccionado,
                    idMasCercano = masCercano?.id,
                    onClick = { id ->
                        vm.seleccionar(id)
                    },
                    modifier = Modifier.height(180.dp)
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

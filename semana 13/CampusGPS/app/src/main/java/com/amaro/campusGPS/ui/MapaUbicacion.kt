
package com.amaro.campusGPS.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.amaro.campusGPS.model.Lugar
import com.amaro.campusGPS.model.LugarConDistancia
import com.amaro.campusGPS.model.UbicacionUi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    modifier: Modifier = Modifier,
    radioMetros: Int = 200
) {

    val modoOscuro = isSystemInDarkTheme()

    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    var mapaCargado by remember {
        mutableStateOf(false)
    }

    val estiloMapa = remember(modoOscuro) {

        val jsonEstilo = if (modoOscuro) {

            """
            [
              {
                "elementType": "labels",
                "stylers": [{"visibility": "off"}]
              },
              {
                "elementType": "geometry",
                "stylers": [{"color": "#1B2633"}]
              },
              {
                "featureType": "landscape",
                "elementType": "geometry",
                "stylers": [{"color": "#202D3C"}]
              },
              {
                "featureType": "road",
                "elementType": "geometry",
                "stylers": [{"color": "#475569"}]
              },
              {
                "featureType": "poi.park",
                "elementType": "geometry",
                "stylers": [{"color": "#244536"}]
              },
              {
                "featureType": "water",
                "elementType": "geometry",
                "stylers": [{"color": "#162E46"}]
              },
              {
                "featureType": "transit",
                "stylers": [{"visibility": "off"}]
              }
            ]
            """.trimIndent()

        } else {

            """
            [
              {
                "elementType": "labels",
                "stylers": [{"visibility": "off"}]
              },
              {
                "featureType": "landscape",
                "elementType": "geometry",
                "stylers": [{"color": "#E9EFF2"}]
              },
              {
                "featureType": "road",
                "elementType": "geometry",
                "stylers": [{"color": "#FFFFFF"}]
              },
              {
                "featureType": "poi.park",
                "elementType": "geometry",
                "stylers": [{"color": "#D6ECD9"}]
              },
              {
                "featureType": "water",
                "elementType": "geometry",
                "stylers": [{"color": "#DCEBF6"}]
              },
              {
                "featureType": "transit",
                "stylers": [{"visibility": "off"}]
              }
            ]
            """.trimIndent()
        }

        MapStyleOptions(jsonEstilo)
    }

    val posicionActual = (
            estado as? UbicacionUi.Lista
            )?.posicion

    val hayUbicacion = posicionActual != null

    LaunchedEffect(
        mapaCargado,
        seleccionado,
        radioMetros,
        hayUbicacion
    ) {

        if (!mapaCargado) {
            return@LaunchedEffect
        }

        val posicion = posicionActual
            ?: return@LaunchedEffect

        val lugarSeleccionado = lugares.firstOrNull {
            it.lugar.id == seleccionado
        }?.lugar

        if (lugarSeleccionado != null) {

            camara.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        lugarSeleccionado.latitud,
                        lugarSeleccionado.longitud
                    ),
                    17f
                ),
                durationMs = 1000
            )

        } else {

            val zoomUbicacion = when (radioMetros) {
                100 -> 17f
                200 -> 16f
                500 -> 15f
                else -> 16f
            }

            camara.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        posicion.latitud,
                        posicion.longitud
                    ),
                    zoomUbicacion
                ),
                durationMs = 1000
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        properties = MapProperties(
            mapStyleOptions = estiloMapa
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        ),
        onMapLoaded = {
            mapaCargado = true
        }
    ) {

        if (estado is UbicacionUi.Lista) {

            Circle(
                center = LatLng(
                    estado.posicion.latitud,
                    estado.posicion.longitud
                ),
                radius = radioMetros.toDouble(),
                fillColor = if (modoOscuro) {
                    Color(0x442196F3)
                } else {
                    Color(0x3312699C)
                },
                strokeColor = if (modoOscuro) {
                    Color(0xFF64B5F6)
                } else {
                    Color(0xFF12699C)
                },
                strokeWidth = 3f,
                clickable = false
            )

            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(
                        estado.posicion.latitud,
                        estado.posicion.longitud
                    )
                ),
                title = "Estás aquí"
            )
        }

        lugares.forEach { item ->

            key(item.lugar.id) {

                val colorMarcador = when {

                    item.lugar.id == masCercano?.id ->
                        BitmapDescriptorFactory.HUE_GREEN

                    item.metros != null &&
                            item.metros > radioMetros.toFloat() ->
                        BitmapDescriptorFactory.HUE_ROSE

                    else ->
                        BitmapDescriptorFactory.HUE_AZURE
                }

                Marker(
                    state = rememberUpdatedMarkerState(
                        position = LatLng(
                            item.lugar.latitud,
                            item.lugar.longitud
                        )
                    ),
                    title = item.lugar.nombre,
                    snippet = item.lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        colorMarcador
                    )
                )
            }
        }
    }
}

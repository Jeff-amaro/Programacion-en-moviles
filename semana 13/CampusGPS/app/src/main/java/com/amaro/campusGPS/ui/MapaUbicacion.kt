
package com.amaro.campusGPS.ui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.amaro.campusGPS.model.LugarConDistancia
import com.amaro.campusGPS.model.UbicacionUi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
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
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    var mapaCargado by remember {
        mutableStateOf(false)
    }

    val estiloMapa = remember {
        MapStyleOptions(
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
        )
    }

    LaunchedEffect(estado, mapaCargado) {
        if (mapaCargado && estado is UbicacionUi.Lista) {
            val posicion = LatLng(
                estado.posicion.latitud,
                estado.posicion.longitud
            )

            camara.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    posicion,
                    16f
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

        // TODO Paso 4: agregar un marcador por cada lugar
    }
}

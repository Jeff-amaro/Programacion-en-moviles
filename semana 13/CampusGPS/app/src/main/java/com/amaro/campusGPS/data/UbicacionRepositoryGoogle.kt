
package com.amaro.campusGPS.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.amaro.campusGPS.model.Posicion
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(
    context: Context
) : UbicacionRepository {

    private val cliente =
        LocationServices.getFusedLocationProviderClient(
            context.applicationContext
        )

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> {
        return runCatching {

            val ubicacion = cliente.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()

            if (ubicacion == null) {
                throw IllegalStateException(
                    "No se pudo obtener la ubicación. Verifica el GPS."
                )
            }

            Posicion(
                latitud = ubicacion.latitude,
                longitud = ubicacion.longitude
            )
        }
    }
}


package com.amaro.campusGPS.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.amaro.campusGPS.model.Posicion
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

class UbicacionNoDisponibleException :
    Exception("Ubicación no disponible")

class UbicacionRepositoryGoogle(
    private val context: Context
) : UbicacionRepository {

    private val cliente =
        LocationServices.getFusedLocationProviderClient(
            context.applicationContext
        )

    override fun playServicesDisponible(): Boolean {
        return GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(
                context.applicationContext
            ) == ConnectionResult.SUCCESS
    }

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> {
        return runCatching {

            val locationManager = context.getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager

            if (!LocationManagerCompat.isLocationEnabled(locationManager)) {
                throw UbicacionNoDisponibleException()
            }


            val ubicacionActual = cliente.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()

            val ubicacion = ubicacionActual
                ?: cliente.lastLocation.await()
                ?: throw UbicacionNoDisponibleException()


            Posicion(
                latitud = ubicacion.latitude,
                longitud = ubicacion.longitude
            )
        }
    }
}

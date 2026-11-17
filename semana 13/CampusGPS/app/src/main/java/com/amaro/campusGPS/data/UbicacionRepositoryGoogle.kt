
package com.amaro.campusGPS.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.amaro.campusGPS.model.Posicion
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    @SuppressLint("MissingPermission")
    override fun observarUbicacion(): Flow<Posicion> = callbackFlow {

        val permisoFino = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val permisoAproximado = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!permisoFino && !permisoAproximado) {
            close(
                SecurityException("Falta el permiso de ubicación")
            )
            return@callbackFlow
        }

        if (!playServicesDisponible()) {
            close(
                IllegalStateException(
                    "Este dispositivo no tiene Google Play Services"
                )
            )
            return@callbackFlow
        }

        val locationManager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        if (!LocationManagerCompat.isLocationEnabled(locationManager)) {
            close(UbicacionNoDisponibleException())
            return@callbackFlow
        }

        val solicitud = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            3000L
        )
            .setMinUpdateIntervalMillis(1500L)
            .setMinUpdateDistanceMeters(2f)
            .build()

        val callback = object : LocationCallback() {

            override fun onLocationResult(
                resultado: LocationResult
            ) {
                resultado.lastLocation?.let { ubicacion ->

                    val posicion = Posicion(
                        latitud = ubicacion.latitude,
                        longitud = ubicacion.longitude
                    )

                    trySend(posicion)
                }
            }
        }

        try {
            cliente.requestLocationUpdates(
                solicitud,
                callback,
                Looper.getMainLooper()
            ).addOnFailureListener { error ->
                close(error)
            }
        } catch (error: Exception) {
            close(error)
            return@callbackFlow
        }

        awaitClose {
            cliente.removeLocationUpdates(callback)
        }
    }
}

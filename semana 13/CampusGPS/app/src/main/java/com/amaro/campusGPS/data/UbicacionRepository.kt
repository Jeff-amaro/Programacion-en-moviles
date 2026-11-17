
package com.amaro.campusGPS.data

import com.amaro.campusGPS.model.Posicion
import kotlinx.coroutines.flow.Flow

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
    fun playServicesDisponible(): Boolean
    fun observarUbicacion(): Flow<Posicion>
}

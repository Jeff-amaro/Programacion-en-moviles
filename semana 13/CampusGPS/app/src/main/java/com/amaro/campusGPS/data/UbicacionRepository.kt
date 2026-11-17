
package com.amaro.campusGPS.data

import com.amaro.campusGPS.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
}

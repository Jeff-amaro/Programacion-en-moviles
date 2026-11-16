
package com.tecsup.amaro.data

import com.tecsup.amaro.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
}

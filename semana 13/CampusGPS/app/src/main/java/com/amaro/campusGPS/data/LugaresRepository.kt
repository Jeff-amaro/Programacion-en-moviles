
package com.amaro.campusGPS.data

import com.amaro.campusGPS.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}

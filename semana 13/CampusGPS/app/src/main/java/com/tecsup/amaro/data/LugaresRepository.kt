
package com.tecsup.amaro.data

import com.tecsup.amaro.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}

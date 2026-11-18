
package com.amaro.centrotareas.data

import kotlinx.coroutines.flow.Flow

data class AvancePrimos(
    val progreso: Float,
    val cantidadFinal: Int? = null
)

interface TareasRepository {
    suspend fun contarPrimos(hasta: Int): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>

    fun tareaConProgreso(): Flow<Float>

    fun contarPrimosConProgreso(hasta: Int): Flow<AvancePrimos>
}

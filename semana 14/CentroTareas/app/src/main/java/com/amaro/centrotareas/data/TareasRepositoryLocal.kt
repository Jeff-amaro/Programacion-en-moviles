
package com.amaro.centrotareas.data

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class TareasRepositoryLocal : TareasRepository {

    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(
        simularError: Boolean
    ): List<String> {
        delay(3000)

        if (simularError) {
            throw IOException("Sin conexión")
        }

        return listOf(
            "Pedido 101",
            "Pedido 102",
            "Pedido 103"
        )
    }

    override suspend fun contarPrimos(
        hasta: Int
    ): Int = withContext(Dispatchers.Default) {

        var cantidad = 0

        for (n in 2..hasta) {
            currentCoroutineContext().ensureActive()

            if (esPrimo(n)) {
                cantidad++
            }
        }

        cantidad
    }

    private fun esPrimo(n: Int): Boolean {

        if (n < 2) return false

        var d = 2

        while (d.toLong() * d <= n) {
            if (n % d == 0) {
                return false
            }

            d++
        }

        return true
    }

    // MEJORA 1: Progreso desde el Repository

    override fun tareaConProgreso(): Flow<Float> = flow {

        Log.d(
            "HILO",
            "Dentro: ${Thread.currentThread().name}"
        )

        for (i in 1..100) {
            delay(40)
            emit(i / 100f)
        }

    }.flowOn(Dispatchers.Default)

    // MEJORA 4: Primos con progreso

    override fun contarPrimosConProgreso(
        hasta: Int
    ): Flow<AvancePrimos> = flow {

        if (hasta < 2) {
            emit(AvancePrimos(1f, 0))
            return@flow
        }

        var cantidad = 0
        val total = hasta - 1
        val intervalo = maxOf(1, total / 100)

        emit(AvancePrimos(0f))

        for (n in 2..hasta) {

            currentCoroutineContext().ensureActive()

            if (esPrimo(n)) {
                cantidad++
            }

            val procesados = n - 1

            if (procesados % intervalo == 0 || n == hasta) {

                val porcentaje =
                    procesados.toFloat() / total

                emit(
                    AvancePrimos(
                        progreso = porcentaje,
                        cantidadFinal =
                            if (n == hasta) cantidad else null
                    )
                )
            }
        }

    }.flowOn(Dispatchers.Default)
}

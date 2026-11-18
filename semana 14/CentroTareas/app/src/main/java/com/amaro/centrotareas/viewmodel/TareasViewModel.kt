
package com.amaro.centrotareas.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amaro.centrotareas.data.TareasRepository
import com.amaro.centrotareas.data.HistorialRepositoryLocal
import com.amaro.centrotareas.data.RegistroHistorial
import com.amaro.centrotareas.model.PrimosUi
import com.amaro.centrotareas.model.CargaUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect

class TareasViewModel(
    private val repo: TareasRepository
) : ViewModel() {

    // Tarea con progreso: pasos 1 al 4

    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    // PASO 3: Progreso de 0 a 100 %

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private var trabajo: Job? = null

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    fun tareaLarga() {

        if (_corriendo.value) return

        _corriendo.value = true

        trabajo = viewModelScope.launch {

            _progreso.value = 0f
            _mensaje.value = "Trabajando..."

            try {

                Log.d(
                    "HILO",
                    "Antes: ${Thread.currentThread().name}"
                )

                // MEJORA 1: Progreso desde el Repository

                repo.tareaConProgreso().collect { valor ->

                    _progreso.value = valor
                }

                Log.d(
                    "HILO",
                    "Despues: ${Thread.currentThread().name}"
                )

                _mensaje.value = "Terminó"

            } finally {

                _corriendo.value = false
            }
        }
    }

    fun cancelar() {

        if (_corriendo.value) {

            trabajo?.cancel()
            _mensaje.value = "Cancelado"
        }
    }

    // Contar primos: paso 5

    private val _primos =
        MutableStateFlow<PrimosUi>(PrimosUi.Inicial)

    val primos: StateFlow<PrimosUi> =
        _primos.asStateFlow()

    private var trabajoPrimos: Job? = null

    // MEJORA 4: Progreso del cálculo de primos

    private val _progresoPrimos = MutableStateFlow(0f)

    val progresoPrimos: StateFlow<Float> =
        _progresoPrimos.asStateFlow()

    fun contar(hasta: Int) {

        if (hasta < 2) return

        if (_primos.value == PrimosUi.Calculando) return

        _progresoPrimos.value = 0f
        _primos.value = PrimosUi.Calculando

        trabajoPrimos = viewModelScope.launch {

            val inicio = System.currentTimeMillis()

            try {

                repo.contarPrimosConProgreso(hasta)
                    .collect { avance ->

                        _progresoPrimos.value =
                            avance.progreso

                        avance.cantidadFinal?.let { cantidad ->

                            val tiempo =
                                System.currentTimeMillis() - inicio

                            _primos.value = PrimosUi.Listo(
                                cantidad,
                                tiempo
                            )

                            // MEJORA 5: Guardar resultado de primos

                            registrarHistorial(
                                tipo = "Primos",
                                detalle = "Hasta $hasta: " +
                                        "$cantidad primos ($tiempo ms)"
                            )
                        }
                    }

            } catch (e: CancellationException) {

                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }

    fun cancelarPrimos() {

        if (_primos.value == PrimosUi.Calculando) {

            trabajoPrimos?.cancel()
            _primos.value = PrimosUi.Cancelado
        }
    }

    // Dos cargas: pasos 6 y 7

    private val _carga =
        MutableStateFlow<CargaUi>(CargaUi.Inicial)

    val carga: StateFlow<CargaUi> =
        _carga.asStateFlow()

    private val _simularError = MutableStateFlow(false)

    val simularError: StateFlow<Boolean> =
        _simularError.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {

        _simularError.value = valor
    }

    // MEJORA 2: Tiempo máximo de 2 segundos

    private val _limiteDosSegundos =
        MutableStateFlow(false)

    val limiteDosSegundos: StateFlow<Boolean> =
        _limiteDosSegundos.asStateFlow()

    fun cambiarLimiteDosSegundos(valor: Boolean) {

        _limiteDosSegundos.value = valor
    }

    private suspend fun <T> ejecutarConLimite(
        bloque: suspend () -> T
    ): T {

        return if (_limiteDosSegundos.value) {

            withTimeout(2000L) {

                bloque()
            }

        } else {

            bloque()
        }
    }

    // MEJORA 3: Reintento automático

    private suspend fun <T> ejecutarConReintentos(
        bloque: suspend () -> T
    ): T {

        val maxReintentos = 3

        for (intento in 0..maxReintentos) {

            try {

                return ejecutarConLimite {

                    bloque()
                }

            } catch (e: TimeoutCancellationException) {

                // Respetar el límite de la Mejora 2
                throw e

            } catch (e: CancellationException) {

                // No reintentar si se cancela
                throw e

            } catch (e: Exception) {

                if (intento == maxReintentos) {

                    throw e
                }

                // Espera creciente: 500, 1000 y 1500 ms

                delay((intento + 1) * 500L)
            }
        }

        error("No se pudo completar la carga")
    }

    fun cargarUnaPorUna() {

        if (_carga.value == CargaUi.Cargando) return

        _carga.value = CargaUi.Cargando

        viewModelScope.launch {

            val inicio = System.currentTimeMillis()

            val errorSimulado = _simularError.value

            try {

                val (perfil, pedidos) =
                    ejecutarConReintentos {

                        val perfilCargado =
                            repo.cargarPerfil()

                        val pedidosCargados =
                            repo.cargarPedidos(errorSimulado)

                        Pair(
                            perfilCargado,
                            pedidosCargados
                        )
                    }

                val tiempo =
                    System.currentTimeMillis() - inicio

                _carga.value = CargaUi.Exito(
                    "Una por una",
                    perfil,
                    pedidos.size,
                    tiempo
                )

                // MEJORA 5: Guardar carga exitosa

                registrarHistorial(
                    tipo = "Carga: Una por una",
                    detalle = "$perfil, " +
                            "${pedidos.size} pedidos ($tiempo ms)"
                )

            } catch (e: TimeoutCancellationException) {

                _carga.value = CargaUi.Error(
                    "Tardó demasiado"
                )

            } catch (e: Exception) {

                if (e is CancellationException) throw e

                _carga.value = CargaUi.Error(
                    "No se pudo cargar: " +
                            "${e.message ?: "Error desconocido"}"
                )
            }
        }
    }

    fun cargarALaVez() {

        if (_carga.value == CargaUi.Cargando) return

        _carga.value = CargaUi.Cargando

        viewModelScope.launch {

            val inicio = System.currentTimeMillis()

            val errorSimulado = _simularError.value

            try {

                val (perfil, pedidos) =
                    ejecutarConReintentos {

                        coroutineScope {

                            val perfilDeferred = async {

                                repo.cargarPerfil()
                            }

                            val pedidosDeferred = async {

                                repo.cargarPedidos(errorSimulado)
                            }

                            Pair(
                                perfilDeferred.await(),
                                pedidosDeferred.await()
                            )
                        }
                    }

                val tiempo =
                    System.currentTimeMillis() - inicio

                _carga.value = CargaUi.Exito(
                    "A la vez",
                    perfil,
                    pedidos.size,
                    tiempo
                )

                // MEJORA 5: Guardar carga exitosa

                registrarHistorial(
                    tipo = "Carga: A la vez",
                    detalle = "$perfil, " +
                            "${pedidos.size} pedidos ($tiempo ms)"
                )

            } catch (e: TimeoutCancellationException) {

                _carga.value = CargaUi.Error(
                    "Tardó demasiado"
                )

            } catch (e: Exception) {

                if (e is CancellationException) throw e

                _carga.value = CargaUi.Error(
                    "No se pudo cargar: " +
                            "${e.message ?: "Error desconocido"}"
                )
            }
        }
    }

    // MEJORA 5: Historial de resultados con DataStore

    private var repoHistorial: HistorialRepositoryLocal? = null

    private val _historial =
        MutableStateFlow<List<RegistroHistorial>>(
            emptyList()
        )

    val historial: StateFlow<List<RegistroHistorial>> =
        _historial.asStateFlow()

    // Inicializar el historial una sola vez

    fun iniciarHistorial(context: Context) {

        if (repoHistorial != null) return

        val repositorio = HistorialRepositoryLocal(
            context.applicationContext
        )

        repoHistorial = repositorio

        // Recuperar resultados al abrir la aplicación

        viewModelScope.launch {

            repositorio.historial.collect { registros ->

                _historial.value = registros
            }
        }
    }

    // Guardar resultados exitosos

    private suspend fun registrarHistorial(
        tipo: String,
        detalle: String
    ) {

        try {

            repoHistorial?.guardar(
                RegistroHistorial(
                    tipo = tipo,
                    detalle = detalle,
                    fechaMillis = System.currentTimeMillis()
                )
            )

        } catch (e: CancellationException) {

            throw e

        } catch (e: Exception) {

            // Si falla DataStore, no perder el resultado
            Log.e(
                "HISTORIAL",
                "No se pudo guardar el resultado",
                e
            )
        }
    }
}

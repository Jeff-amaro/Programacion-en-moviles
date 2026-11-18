package com.amaro.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amaro.centrotareas.data.TareasRepository
import com.amaro.centrotareas.model.PrimosUi
import com.amaro.centrotareas.model.CargaUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

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

                withContext(Dispatchers.Default) {

                    Log.d(
                        "HILO",
                        "Dentro: ${Thread.currentThread().name}"
                    )

                    for (i in 1..100) {
                        delay(40)
                        _progreso.value = i / 100f
                    }
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
    fun contar(hasta: Int) {
        if (hasta < 2) return
        if (_primos.value == PrimosUi.Calculando) return
        _primos.value = PrimosUi.Calculando
        trabajoPrimos = viewModelScope.launch {
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta)
                val tiempo =
                    System.currentTimeMillis() - inicio
                _primos.value = PrimosUi.Listo(
                    cantidad,
                    tiempo
                )
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


    fun cargarUnaPorUna() {
        if (_carga.value == CargaUi.Cargando) return
        _carga.value = CargaUi.Cargando
        viewModelScope.launch {
            val inicio = System.currentTimeMillis()
            val errorSimulado = _simularError.value
            try {
                val perfil = repo.cargarPerfil()
                val pedidos = repo.cargarPedidos(errorSimulado)
                val tiempo =
                    System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito(
                    "Una por una",
                    perfil,
                    pedidos.size,
                    tiempo
                )
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _carga.value = CargaUi.Error(
                    "No se pudo cargar: ${e.message ?: "Error desconocido"}"
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
                val (perfil, pedidos) = coroutineScope {
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

                val tiempo =
                    System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito(
                    "A la vez",
                    perfil,
                    pedidos.size,
                    tiempo
                )
            } catch (e: Exception) {

                if (e is CancellationException) throw e

                _carga.value = CargaUi.Error(
                    "No se pudo cargar: ${e.message ?: "Error desconocido"}"
                )
            }
        }
    }
}

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

class TareasViewModel(
    private val repo: TareasRepository
) : ViewModel() {

    // Tarea con progreso: pasos 1 al 4

    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    // PASO 3: Progreso de 0 a 100 %

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    fun tareaLarga() {
        viewModelScope.launch {

            _progreso.value = 0f
            _mensaje.value = "Trabajando..."

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
        }
    }

    // TODO Paso 4: trabajo, corriendo y cancelar()

    // Contar primos: paso 5

    private val _primos =
        MutableStateFlow<PrimosUi>(PrimosUi.Inicial)

    val primos: StateFlow<PrimosUi> =
        _primos.asStateFlow()

    // TODO Paso 5: contar() y cancelarPrimos()

    // Dos cargas: pasos 6 y 7

    private val _carga =
        MutableStateFlow<CargaUi>(CargaUi.Inicial)

    val carga: StateFlow<CargaUi> =
        _carga.asStateFlow()

    // TODO Paso 6: cargarUnaPorUna() y cargarALaVez()
    // TODO Paso 7: simularError y manejo de errores
}

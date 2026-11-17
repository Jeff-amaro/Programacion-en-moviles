
package com.amaro.campusGPS.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amaro.campusGPS.data.LugaresRepository
import com.amaro.campusGPS.data.UbicacionRepository
import com.amaro.campusGPS.data.UbicacionNoDisponibleException
import com.amaro.campusGPS.data.UltimoLugarRepository
import com.amaro.campusGPS.model.Lugar
import com.amaro.campusGPS.model.LugarConDistancia
import com.amaro.campusGPS.model.Posicion
import com.amaro.campusGPS.model.TipoError
import com.amaro.campusGPS.model.UbicacionUi
import com.amaro.campusGPS.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository,
    private val ultimoLugarRepo: UltimoLugarRepository
) : ViewModel() {

    private val _estado =
        MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)

    val estado: StateFlow<UbicacionUi> =
        _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map {
            LugarConDistancia(it, null)
        }
    )

    val lugares: StateFlow<List<LugarConDistancia>> =
        _lugares.asStateFlow()

    private val _seleccionado =
        MutableStateFlow<Int?>(null)

    val seleccionado: StateFlow<Int?> =
        _seleccionado.asStateFlow()

    val masCercano: StateFlow<Lugar?> = lugares
        .map { lista ->
            lista.firstOrNull {
                it.metros != null
            }?.lugar
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    private val _radioMetros = MutableStateFlow(200)

    val radioMetros: StateFlow<Int> =
        _radioMetros.asStateFlow()

    val lugaresEnRadio: StateFlow<List<LugarConDistancia>> =
        combine(
            lugares,
            radioMetros,
            estado
        ) { lista, radio, estadoActual ->

            if (estadoActual is UbicacionUi.Lista) {
                lista.filter { lugar ->
                    lugar.metros?.let { distancia ->
                        distancia <= radio.toFloat()
                    } ?: false
                }
            } else {
                emptyList()
            }

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val ultimoLugar: StateFlow<Lugar?> =
        ultimoLugarRepo.ultimoLugarId
            .map { id ->
                lugaresRepo.obtenerLugares()
                    .firstOrNull { lugar ->
                        lugar.id == id
                    }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                null
            )

    fun cambiarRadio(metros: Int) {
        if (metros in listOf(100, 200, 500)) {
            _radioMetros.value = metros
        }
    }

    fun buscar() {

        _seleccionado.value = null

        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                mensaje = "Este dispositivo no tiene Google Play Services",
                tipo = TipoError.SIN_PLAY_SERVICES
            )
            return
        }

        _estado.value = UbicacionUi.Buscando

        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->
                    actualizarDistancias(posicion)
                    _estado.value = UbicacionUi.Lista(posicion)
                }
                .onFailure { error ->

                    val tipo = when (error) {
                        is SecurityException ->
                            TipoError.SIN_PERMISO

                        is UbicacionNoDisponibleException ->
                            TipoError.GPS_APAGADO

                        else ->
                            TipoError.OTRO
                    }

                    val mensaje = when (tipo) {
                        TipoError.SIN_PERMISO ->
                            "Sin permiso de ubicación no puedo mostrar dónde estás"

                        TipoError.GPS_APAGADO ->
                            "No se pudo obtener la ubicación.\nActiva el GPS e intenta de nuevo"

                        TipoError.SIN_PLAY_SERVICES ->
                            "Este dispositivo no tiene Google Play Services"

                        TipoError.OTRO ->
                            error.message ?: "No se pudo obtener la ubicación"
                    }

                    _estado.value = UbicacionUi.Error(
                        mensaje = mensaje,
                        tipo = tipo
                    )
                }
        }
    }

    private fun actualizarDistancias(pos: Posicion) {

        val listaOrdenada = lugaresRepo.obtenerLugares()
            .map { lugar ->
                LugarConDistancia(
                    lugar = lugar,
                    metros = distanciaMetros(pos, lugar)
                )
            }
            .sortedBy { it.metros }

        _lugares.value = listaOrdenada
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            mensaje = "Sin permiso de ubicación no puedo mostrar dónde estás",
            tipo = TipoError.SIN_PERMISO
        )
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id

        if (id != null) {
            viewModelScope.launch {
                ultimoLugarRepo.guardarUltimoLugar(id)
            }
        }
    }

    fun irAlUltimoLugar() {
        if (_estado.value is UbicacionUi.Lista) {
            _seleccionado.value = ultimoLugar.value?.id
        }
    }
}

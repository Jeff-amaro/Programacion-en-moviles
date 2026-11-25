package com.amaro.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import com.amaro.avisoscampus.data.AvisosRepository
import com.amaro.avisoscampus.model.Aviso
import com.amaro.avisoscampus.model.PermisoUi
import kotlinx.coroutines.flow.*

class AvisosViewModel(
    private val repo: AvisosRepository
) : ViewModel() {

    val avisos: StateFlow<List<Aviso>> = repo.avisos

    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // TODO Paso 2: permisoRespondido(concedido: Boolean)

    // TODO Paso 3: avisarAhora()

    // TODO Paso 5: faltan, recordarEn y cancelarRecordatorio()

    // TODO Paso 6: escuchandoAvion, cambiarEscucha y alCambiarModoAvion()

    // TODO Paso 8: noSePudoMostrar()
}

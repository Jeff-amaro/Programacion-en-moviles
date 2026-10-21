package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: CafeRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    val perfilActivo: StateFlow<String?> = preferenciasStore.perfilActivo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "ADMIN")

    val modoOscuro: StateFlow<Boolean> = preferenciasStore.modoOscuro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val notificaciones: StateFlow<Boolean> = preferenciasStore.notificaciones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val recibirPedidos: StateFlow<Boolean> = preferenciasStore.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val usuarioActual: StateFlow<Usuario?> = MutableStateFlow(
        Usuario(
            id = 2,
            nombre = "Juan Pérez",
            correo = "juan.perez@tecsup.edu.pe",
            clave = "123456",
            rol = "ESTUDIANTE"
        )
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        Usuario(
            id = 2,
            nombre = "Juan Pérez",
            correo = "juan.perez@tecsup.edu.pe",
            clave = "123456",
            rol = "ESTUDIANTE"
        )
    )

    val error = MutableStateFlow<String?>(null)

    fun limpiarError() {
        error.value = null
    }

    fun login(correo: String, clave: String, rol: String, onExito: (String) -> Unit) {
        viewModelScope.launch {
            if (correo.isBlank() || clave.isBlank()) {
                error.value = "Por favor, completa todos los campos"
                return@launch
            }
            preferenciasStore.guardarPerfilActivo(rol)
            error.value = null
            onExito(rol)
        }
    }

    fun registrarEstudiante(
        nombre: String,
        correo: String,
        clave: String,
        onExito: () -> Unit
    ) {
        viewModelScope.launch {
            if (nombre.isBlank() || correo.isBlank() || clave.isBlank()) {
                error.value = "Por favor, completa todos los campos"
                return@launch
            }

            val existe = repository.buscarPorCorreo(correo.trim())
            if (existe != null) {
                error.value = "El correo ya se encuentra registrado"
                return@launch
            }

            val nuevoUsuario = Usuario(
                nombre = nombre.trim(),
                correo = correo.trim(),
                clave = clave.trim(),
                rol = "ESTUDIANTE"
            )

            repository.registrarUsuario(nuevoUsuario)
            error.value = null
            onExito()
        }
    }

    fun cambiarPerfil(rol: String) {
        viewModelScope.launch {
            preferenciasStore.guardarPerfilActivo(rol)
        }
    }

    fun setModoOscuro(activo: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarModoOscuro(activo)
        }
    }

    fun setNotificaciones(activo: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarNotificaciones(activo)
        }
    }

    fun setRecibirPedidos(activo: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarRecibirPedidos(activo)
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            preferenciasStore.limpiarSesion()
        }
    }

    class Factory(
        private val repository: CafeRepository,
        private val preferenciasStore: PreferenciasStore
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SesionViewModel(repository, preferenciasStore) as T
        }
    }
}
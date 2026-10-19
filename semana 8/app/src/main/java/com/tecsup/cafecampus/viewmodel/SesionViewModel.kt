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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: CafeRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val perfilActivo: StateFlow<String> = preferenciasStore.perfilActivo
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    val modoOscuro: StateFlow<Boolean> = preferenciasStore.modoOscuro
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun login(
        correo: String,
        clave: String,
        rolSeleccionado: String,
        onSuccess: (String) -> Unit
    ) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Por favor, ingresa todos los campos"
            return
        }

        viewModelScope.launch {
            val usuario = repository.login(correo, clave)
            if (usuario == null) {
                _error.value = "Correo o contraseña incorrectos"
            } else if (usuario.rol != rolSeleccionado) {
                _error.value = "El usuario no corresponde al rol seleccionado"
            } else {
                _error.value = null
                _usuarioActual.value = usuario
                preferenciasStore.guardarPerfil(usuario.rol)
                onSuccess(usuario.rol)
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun cerrarSesion(onLogout: () -> Unit) {
        viewModelScope.launch {
            preferenciasStore.cerrarSesion()
            _usuarioActual.value = null
            onLogout()
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
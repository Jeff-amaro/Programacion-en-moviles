package com.tecsup.productos.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.auth.AuthRepository
import com.tecsup.productos.data.auth.FirebaseAuthRepository
import com.tecsup.productos.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginEstado {
    data object Inicial : LoginEstado
    data object Cargando : LoginEstado
    data class Exito(val sesion: Sesion) : LoginEstado
    data class Error(val mensaje: String) : LoginEstado
}

class LoginViewModel(
    private val authRepository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow<LoginEstado>(LoginEstado.Inicial)
    val estado: StateFlow<LoginEstado> = _estado.asStateFlow()

    fun login(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _estado.value = LoginEstado.Error("El correo y la contraseña no pueden estar vacíos")
            return
        }

        _estado.value = LoginEstado.Cargando
        viewModelScope.launch {
            authRepository.iniciarSesion(correo, clave)
                .onSuccess { sesion ->
                    _estado.value = LoginEstado.Exito(sesion)
                }
                .onFailure {
                    _estado.value = LoginEstado.Error("Credenciales incorrectas o error de red")
                }
        }
    }

    fun verificarSesion() {
        viewModelScope.launch {
            val sesion = authRepository.sesionActual()
            if (sesion != null) {
                _estado.value = LoginEstado.Exito(sesion)
            }
        }
    }
}
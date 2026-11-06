package com.tecsup.productos.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.auth.AuthRepository
import com.tecsup.productos.data.auth.FirebaseAuthRepository
import com.tecsup.productos.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: ProductoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _totalGuardados = MutableStateFlow(0)
    val totalGuardados: StateFlow<Int> = _totalGuardados.asStateFlow()

    private val _totalFavoritos = MutableStateFlow(0)
    val totalFavoritos: StateFlow<Int> = _totalFavoritos.asStateFlow()

    init {
        actualizarTotales()
    }

    fun actualizarTotales() {
        viewModelScope.launch {
            _totalGuardados.value = repository.cantidadGuardada()
            _totalFavoritos.value = repository.cantidadFavoritos()
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            repository.refrescar()
            actualizarTotales()
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repository.limpiarCache()
            actualizarTotales()
        }
    }

    fun cerrarSesion() {
        authRepository.cerrarSesion()
    }

    companion object {
        val Factory: androidx.lifecycle.ViewModelProvider.Factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: androidx.lifecycle.viewmodel.CreationExtras
            ): T {
                val application = checkNotNull(extras[androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val api = com.tecsup.productos.data.remote.RetrofitClient.api
                val db = com.tecsup.productos.data.local.AppDatabase.obtenerInstancia(application)
                val repo = com.tecsup.productos.data.repository.ProductoRepositoryImpl(api, db.productoDao())
                val authRepo = FirebaseAuthRepository()
                return AdminViewModel(repo, authRepo) as T
            }
        }
    }
}
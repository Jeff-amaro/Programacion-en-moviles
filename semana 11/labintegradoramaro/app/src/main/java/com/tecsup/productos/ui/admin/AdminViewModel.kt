package com.tecsup.productos.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.auth.AuthRepository
import com.tecsup.productos.data.auth.FirebaseAuthRepository
import com.tecsup.productos.data.repository.ProductoRepository
import com.tecsup.productos.notification.Notificador
import com.tecsup.productos.notification.NotificadorAndroid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.tecsup.productos.data.remote.RetrofitClient
import com.tecsup.productos.data.repository.ProductoRepositoryImpl

class AdminViewModel(
    private val repository: ProductoRepository,
    private val authRepository: AuthRepository,
    private val notificador: Notificador
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
            val resultado = repository.refrescar()
            actualizarTotales()

            resultado.onSuccess { n ->
                notificador.mostrar("Sincronización", "Se actualizaron $n productos desde la red")
            }.onFailure {
                notificador.mostrar("Error", "No se pudo sincronizar el catálogo")
            }
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repository.limpiarCache()
            actualizarTotales()
            notificador.mostrar("Caché Limpia", "Se eliminaron los datos locales no favoritos")
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
                val api = RetrofitClient.api
                val db = com.tecsup.productos.data.local.AppDatabase.obtenerInstancia(context = application)
                val repo = ProductoRepositoryImpl(
                    api = api,
                    dao = db.productoDao(),
                    carritoDao = db.carritoDao(),
                    historialDao = db.historialDao(),
                    favoritoDao = db.favoritoDao(),
                )
                val authRepo = FirebaseAuthRepository()
                val notificador = NotificadorAndroid(application)

                return AdminViewModel(repo, authRepo, notificador) as T
            }
        }
    }
}
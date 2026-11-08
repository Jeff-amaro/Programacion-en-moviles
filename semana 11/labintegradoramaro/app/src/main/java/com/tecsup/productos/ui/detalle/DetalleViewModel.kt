package com.tecsup.productos.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.local.AppDatabase
import com.tecsup.productos.data.remote.RetrofitClient
import com.tecsup.productos.data.repository.ProductoRepository
import com.tecsup.productos.data.repository.ProductoRepositoryImpl
import com.tecsup.productos.domain.model.Producto
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.notification.Notificador
import com.tecsup.productos.notification.NotificadorAndroid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val repo: ProductoRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Producto>>(UiState.Cargando)
    val estado: StateFlow<UiState<Producto>> = _estado.asStateFlow()

    fun cargarDetalle(id: Int) {
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            val resultado = repo.obtenerDetalle(id)
            resultado.onSuccess { producto ->
                _estado.value = UiState.Exito(producto)
                // Registramos la vista en el historial automáticamente
                repo.registrarVista(id)
            }.onFailure { e ->
                _estado.value = UiState.Error(e.message ?: "Error al cargar detalle")
            }
        }
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            repo.alternarFavorito(id)
            val estadoActual = _estado.value
            if (estadoActual is UiState.Exito) {
                val productoActualizado = estadoActual.datos.copy(
                    esFavorito = !estadoActual.datos.esFavorito
                )
                _estado.value = UiState.Exito(productoActualizado)
            }
        }
    }

    fun agregarAlCarrito(producto: Producto) {
        viewModelScope.launch {
            repo.agregarAlCarrito(producto)
            notificador.mostrar("Carrito", "${producto.titulo} añadido al carrito")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: androidx.lifecycle.viewmodel.CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val api = RetrofitClient.api
                val db = AppDatabase.obtenerInstancia(application)
                val repo = ProductoRepositoryImpl(
                    api = api,
                    dao = db.productoDao(),
                    carritoDao = db.carritoDao(),
                    historialDao = db.historialDao()
                )
                val notificador = NotificadorAndroid(application)

                return DetalleViewModel(repo, notificador) as T
            }
        }
    }
}
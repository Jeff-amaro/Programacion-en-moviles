package com.tecsup.productos.ui.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.local.CarritoEntity
import com.tecsup.productos.data.repository.ProductoRepository
import com.tecsup.productos.notification.Notificador
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.tecsup.productos.data.remote.RetrofitClient
import com.tecsup.productos.data.repository.ProductoRepositoryImpl

class CarritoViewModel(
    private val repository: ProductoRepository,
    private val notificador: Notificador
) : ViewModel() {

    val itemsCarrito: StateFlow<List<CarritoEntity>> = repository.carrito
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCarrito: StateFlow<Double?> = repository.totalCarrito
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun incrementar(id: Int) {
        viewModelScope.launch {
            repository.incrementarCantidadCarrito(id, 1)
        }
    }

    fun disminuir(idProducto: Int, cantidadActual: Int) {
        viewModelScope.launch {
            if (cantidadActual > 1) {
                repository.incrementarCantidadCarrito(idProducto, -1)
            } else {
                repository.eliminarDelCarrito(idProducto)
            }
        }
    }

    fun eliminarItem(id: Int) {
        viewModelScope.launch {
            repository.eliminarDelCarrito(id)
        }
    }

    fun realizarCheckout(onExito: () -> Unit) {
        viewModelScope.launch {
            repository.vaciarCarrito()
            notificador.mostrar("Compra Exitoso", "¡Tu pedido ha sido procesado con éxito!")
            onExito()
        }
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
                val notificador = com.tecsup.productos.notification.NotificadorAndroid(application)

                return CarritoViewModel(repo, notificador) as T
            }
        }
    }
}
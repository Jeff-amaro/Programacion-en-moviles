package com.tecsup.productos.ui.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ProductoRepository
import com.tecsup.productos.domain.model.Producto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistorialViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    val historialProductos: StateFlow<List<Producto>> = combine(
        repository.historial,
        repository.items
    ) { historialList, productosList ->
        val historialOrdenado = historialList.sortedByDescending { it.timestamp }
        historialOrdenado.mapNotNull { hist ->
            productosList.find { it.id == hist.idProducto }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun limpiarHistorial() {
        viewModelScope.launch {
            repository.limpiarHistorial()
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
                val api = com.tecsup.productos.data.remote.RetrofitClient.api
                val db = com.tecsup.productos.data.local.AppDatabase.obtenerInstancia(application)
                val repo = com.tecsup.productos.data.repository.ProductoRepositoryImpl(
                    api = com.tecsup.productos.data.remote.RetrofitClient.api,
                    dao = db.productoDao(),
                    carritoDao = db.carritoDao(),
                    historialDao = db.historialDao(),
                    favoritoDao = db.favoritoDao(),
                )

                return HistorialViewModel(repo) as T
            }
        }
    }
}
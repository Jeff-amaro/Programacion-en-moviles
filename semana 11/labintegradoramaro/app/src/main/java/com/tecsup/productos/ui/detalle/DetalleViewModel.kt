package com.tecsup.productos.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.remote.RetrofitClient
import com.tecsup.productos.data.repository.ProductoRepository
import com.tecsup.productos.data.repository.ProductoRepositoryImpl
import com.tecsup.productos.domain.model.Producto
import com.tecsup.productos.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val repo: ProductoRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Producto>>(UiState.Cargando)
    val estado: StateFlow<UiState<Producto>> = _estado.asStateFlow()

    fun cargarDetalle(id: Int) {
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            val resultado = repo.obtenerDetalle(id)
            resultado.onSuccess { producto ->
                _estado.value = UiState.Exito(producto)
            }.onFailure { e ->
                _estado.value = UiState.Error(e.message ?: "Error al cargar detalle")
            }
        }
    }

    companion object {
        val Factory: androidx.lifecycle.ViewModelProvider.Factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(
                modelClass: Class<T>,
                extras: androidx.lifecycle.viewmodel.CreationExtras
            ): T {
                val application = checkNotNull(extras[androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                val api = com.tecsup.productos.data.remote.RetrofitClient.api
                val db = com.tecsup.productos.data.local.AppDatabase.obtenerInstancia(application)

                val repo = com.tecsup.productos.data.repository.ProductoRepositoryImpl(api, db.productoDao())
                return modelClass.getConstructor(com.tecsup.productos.data.repository.ProductoRepository::class.java).newInstance(repo)
            }
        }
    }
}
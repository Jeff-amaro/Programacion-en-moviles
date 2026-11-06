package com.tecsup.productos.ui.lista

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
import retrofit2.HttpException
import java.io.IOException

class ListaViewModel(
    private val repo: ProductoRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Producto>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Producto>>> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            repo.items.collect { lista ->
                if (lista.isNotEmpty()) _estado.value = UiState.Exito(lista)
            }
        }
        refrescar()
    }

    fun refrescar() {
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            val resultado = repo.refrescar()
            resultado.onFailure { e ->
                val mensaje = when (e) {
                    is IOException -> "No hay conexión a internet"
                    is HttpException -> "Error del servidor: ${e.code()}"
                    else -> "Ocurrió un error inesperado"
                }
                _estado.value = UiState.Error(mensaje)
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
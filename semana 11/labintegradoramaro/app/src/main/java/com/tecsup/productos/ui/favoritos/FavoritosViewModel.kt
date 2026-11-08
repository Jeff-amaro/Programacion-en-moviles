package com.tecsup.productos.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ProductoRepository
import com.tecsup.productos.domain.model.Producto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.tecsup.productos.data.remote.RetrofitClient
import com.tecsup.productos.data.repository.ProductoRepositoryImpl

class FavoritosViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    val favoritos: StateFlow<List<Producto>> = repository.favoritos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            repository.alternarFavorito(id)
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
                val db = com.tecsup.productos.data.local.AppDatabase.obtenerInstancia(context = application)
                val repo = ProductoRepositoryImpl(
                    api = api,
                    dao = db.productoDao(),
                    carritoDao = db.carritoDao(),
                    historialDao = db.historialDao(),
                    favoritoDao = db.favoritoDao(),
                )
                return FavoritosViewModel(repository = repo) as T
            }
        }
    }
}
package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MenuAdminViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    val categoriaSeleccionada = MutableStateFlow("Todo")

    @OptIn(ExperimentalCoroutinesApi::class)
    val productos: StateFlow<List<Producto>> = categoriaSeleccionada
        .flatMapLatest { cat ->
            repository.observarTodosLosProductos().map { lista ->
                if (cat == "Todo") lista
                else lista.filter { it.categoria.equals(cat, ignoreCase = true) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun seleccionarCategoria(categoria: String) {
        categoriaSeleccionada.value = categoria
    }

    fun guardar(producto: Producto) {
        viewModelScope.launch {
            repository.guardarProducto(producto)
        }
    }

    fun eliminar(producto: Producto) {
        viewModelScope.launch {
            repository.eliminarProducto(producto)
        }
    }

    fun alternarDisponibilidad(producto: Producto) {
        viewModelScope.launch {
            repository.alternarDisponibilidad(producto)
        }
    }

    class Factory(private val repository: CafeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MenuAdminViewModel(repository) as T
        }
    }
}
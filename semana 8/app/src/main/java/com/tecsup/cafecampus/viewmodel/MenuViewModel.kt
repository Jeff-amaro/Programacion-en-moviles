package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    val productos: StateFlow<List<Producto>> = repository.observarProductos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    fun agregar(producto: Producto, cantidad: Int, nota: String) {
        val listaActual = _carrito.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (index != -1) {
            val itemExistente = listaActual[index]
            listaActual[index] = itemExistente.copy(
                cantidad = itemExistente.cantidad + cantidad,
                notas = if (nota.isNotBlank()) nota else itemExistente.notas
            )
        } else {
            listaActual.add(ItemCarrito(producto = producto, cantidad = cantidad, notas = nota))
        }
        _carrito.value = listaActual
    }

    fun quitar(productoId: Int) {
        _carrito.value = _carrito.value.filterNot { it.producto.id == productoId }
    }

    fun vaciar() {
        _carrito.value = emptyList()
    }

    fun total(): Double {
        return _carrito.value.sumOf { it.producto.precio * it.cantidad }
    }

    class Factory(private val repository: CafeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MenuViewModel(repository) as T
        }
    }
}
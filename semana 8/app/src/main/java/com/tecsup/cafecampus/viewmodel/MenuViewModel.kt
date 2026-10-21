package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.relation.DetalleConProducto
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _categoriaSeleccionada = MutableStateFlow("Todo")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda

    val productos: StateFlow<List<Producto>> = combine(
        repository.observarTodosLosProductos(),
        _categoriaSeleccionada,
        _busqueda
    ) { listaProductos, categoria, textoBusqueda ->
        listaProductos.filter { producto ->
            val coincideCategoria = if (categoria == "Todo") true else producto.categoria.equals(categoria, ignoreCase = true)
            val coincideBusqueda = if (textoBusqueda.isBlank()) true else producto.nombre.contains(textoBusqueda, ignoreCase = true)
            coincideCategoria && coincideBusqueda && producto.disponible
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito

    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }

    fun buscarProducto(texto: String) {
        _busqueda.value = texto
    }

    fun agregar(producto: Producto, cantidad: Int = 1, nota: String = "") {
        val itemActual = _carrito.value.find { it.producto.id == producto.id }
        if (itemActual != null) {
            _carrito.value = _carrito.value.map {
                if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
            }
        } else {
            _carrito.value = _carrito.value + ItemCarrito(producto, cantidad)
        }
    }

    fun repetirPedido(detalles: List<DetalleConProducto>) {
        _carrito.value = detalles.map {
            ItemCarrito(
                producto = it.producto,
                cantidad = it.detalle.cantidad
            )
        }
    }

    fun eliminarDelCarrito(item: ItemCarrito) {
        _carrito.value = _carrito.value.filterNot { it == item }
    }

    fun vaciar() {
        _carrito.value = emptyList()
    }

    fun vaciarCarrito() {
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
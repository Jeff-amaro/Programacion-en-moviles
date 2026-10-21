package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.model.PedidosPorHora
import com.tecsup.cafecampus.data.model.ProductoMasVendido
import com.tecsup.cafecampus.data.model.TotalPorFormaPago
import com.tecsup.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PanelAdminViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    val todosLosPedidos: StateFlow<List<PedidoConDetalle>> = repository.observarTodosLosPedidos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pedidosPorHora: StateFlow<List<PedidosPorHora>> = repository.observarPedidosPorHora()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val productoMasVendido: StateFlow<ProductoMasVendido?> = repository.observarProductoMasVendido()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val totalPorFormaPago: StateFlow<List<TotalPorFormaPago>> = repository.observarTotalPorFormaPago()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val nuevos: StateFlow<Int> = todosLosPedidos.map { lista ->
        lista.count { it.pedido.estado == "PENDIENTE" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val preparando: StateFlow<Int> = todosLosPedidos.map { lista ->
        lista.count { it.pedido.estado == "EN_PREPARACION" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val listos: StateFlow<Int> = todosLosPedidos.map { lista ->
        lista.count { it.pedido.estado == "LISTO" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val ventasHoy: StateFlow<Double> = todosLosPedidos.map { lista ->
        lista.filter { it.pedido.estado != "CANCELADO" }.sumOf { it.pedido.total }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val nuevosPedidosPreview: StateFlow<List<PedidoConDetalle>> = todosLosPedidos.map { lista ->
        lista.filter { it.pedido.estado == "PENDIENTE" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun avanzarEstado(pedidoConDetalle: PedidoConDetalle) {
        viewModelScope.launch {
            val siguienteEstado = when (pedidoConDetalle.pedido.estado) {
                "PENDIENTE" -> "EN_PREPARACION"
                "EN_PREPARACION" -> "LISTO"
                "LISTO" -> "ENTREGADO"
                else -> pedidoConDetalle.pedido.estado
            }
            repository.actualizarEstadoPedido(pedidoConDetalle.pedido.id, siguienteEstado)
        }
    }

    class Factory(private val repository: CafeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PanelAdminViewModel(repository) as T
        }
    }
}
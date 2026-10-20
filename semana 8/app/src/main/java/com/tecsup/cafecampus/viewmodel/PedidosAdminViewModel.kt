package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosAdminViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    val estadoSeleccionado = MutableStateFlow("PENDIENTE")
    val pedidoIdSeleccionado = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val pedidos: StateFlow<List<PedidoConDetalle>> = estadoSeleccionado
        .flatMapLatest { estado ->
            repository.observarTodosLosPedidos().map { lista ->
                lista.filter { it.pedido.estado == estado }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val pedidoDetalle: StateFlow<PedidoConDetalle?> = pedidoIdSeleccionado
        .flatMapLatest { id ->
            if (id != null) {
                repository.observarTodosLosPedidos().map { lista ->
                    lista.find { it.pedido.id == id }
                }
            } else {
                flowOf(null)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun seleccionarEstado(estado: String) {
        estadoSeleccionado.value = estado
    }

    fun seleccionarPedido(id: Int) {
        pedidoIdSeleccionado.value = id
    }

    fun avanzar(pedidoConDetalle: PedidoConDetalle) {
        viewModelScope.launch {
            val nuevoEstado = obtenerSiguienteEstado(pedidoConDetalle.pedido.estado)
            repository.actualizarEstadoPedido(pedidoConDetalle.pedido.id, nuevoEstado)
        }
    }

    fun cambiarEstadoManual(pedidoConDetalle: PedidoConDetalle, nuevoEstado: String) {
        viewModelScope.launch {
            repository.actualizarEstadoPedido(pedidoConDetalle.pedido.id, nuevoEstado)
        }
    }

    fun cancelar(pedidoConDetalle: PedidoConDetalle) {
        viewModelScope.launch {
            repository.actualizarEstadoPedido(pedidoConDetalle.pedido.id, "CANCELADO")
        }
    }

    private fun obtenerSiguienteEstado(actual: String): String {
        return when (actual) {
            "PENDIENTE" -> "EN_PREPARACION"
            "EN_PREPARACION" -> "LISTO"
            "LISTO" -> "ENTREGADO"
            else -> actual
        }
    }

    class Factory(private val repository: CafeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PedidosAdminViewModel(repository) as T
        }
    }
}
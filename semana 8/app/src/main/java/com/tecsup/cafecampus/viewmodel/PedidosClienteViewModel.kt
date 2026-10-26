package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosClienteViewModel(
    private val repository: CafeRepository,
    private val clienteId: Int
) : ViewModel() {

    val misPedidos: StateFlow<List<PedidoConDetalle>> = repository.observarPedidosPorCliente(clienteId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _pedidoIdSeleccionado = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val pedidoActual: StateFlow<PedidoConDetalle?> = _pedidoIdSeleccionado
        .flatMapLatest { id ->
            if (id != null) repository.observarPedido(id) else flowOf(null)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun seleccionarPedido(id: Int) {
        _pedidoIdSeleccionado.value = id
    }

    fun cancelarPedido(id: Int) {
        viewModelScope.launch {
            repository.actualizarEstadoPedido(id, "CANCELADO")
        }
    }

    fun confirmar(
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String,
        onExito: (Int) -> Unit
    ) {
        viewModelScope.launch {
            val idNuevo = repository.crearPedido(clienteId, items)
            _pedidoIdSeleccionado.value = idNuevo.toInt()
            onExito(idNuevo.toInt())
        }
    }

    class Factory(
        private val repository: CafeRepository,
        private val clienteId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PedidosClienteViewModel(repository, clienteId) as T
        }
    }
}
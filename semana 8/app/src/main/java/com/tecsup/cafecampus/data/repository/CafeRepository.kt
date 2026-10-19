package com.tecsup.cafecampus.data.repository

import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

interface CafeRepository {
    fun observarProductos(): Flow<List<Producto>>
    suspend fun insertarProducto(producto: Producto): Long
    suspend fun login(correo: String, clave: String): Usuario?
    suspend fun crearPedido(clienteId: Int, items: List<ItemCarrito>): Long
    suspend fun cambiarEstado(id: Int, estado: EstadoPedido): Int
    fun observarPedidosPorCliente(clienteId: Int): Flow<List<PedidoConDetalle>>
    fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>>
    fun observarPedido(id: Int): Flow<PedidoConDetalle?>
    fun observarVentasHoy(inicioMillis: Long): Flow<Double>
    fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int>
}
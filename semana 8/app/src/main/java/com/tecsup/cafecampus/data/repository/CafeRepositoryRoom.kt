package com.tecsup.cafecampus.data.repository

import com.tecsup.cafecampus.data.local.dao.PedidoDao
import com.tecsup.cafecampus.data.local.dao.ProductoDao
import com.tecsup.cafecampus.data.local.dao.UsuarioDao
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

class CafeRepositoryRoom(
    private val productoDao: ProductoDao,
    private val usuarioDao: UsuarioDao,
    private val pedidoDao: PedidoDao
) : CafeRepository {

    override fun observarProductos(): Flow<List<Producto>> {
        return productoDao.obtenerTodos()
    }

    override suspend fun insertarProducto(producto: Producto): Long {
        return productoDao.insertar(producto)
    }

    override suspend fun login(correo: String, clave: String): Usuario? {
        return usuarioDao.login(correo, clave)
    }

    override suspend fun crearPedido(
        clienteId: Int,
        items: List<ItemCarrito>
    ): Long {
        val total = items.sumOf { it.producto.precio * it.cantidad }

        val pedido = Pedido(
            clienteId = clienteId,
            total = total,
            estado = EstadoPedido.PENDIENTE.name,
            fecha = System.currentTimeMillis(),
            pago = "Efectivo",
            horaRecojo = "",
            nota = ""
        )

        val detalles = items.map { item ->
            DetallePedido(
                pedidoId = 0,
                productoId = item.producto.id,
                nombreProducto = item.producto.nombre,
                precioUnitario = item.producto.precio,
                cantidad = item.cantidad
            )
        }

        return pedidoDao.crearPedido(pedido, detalles)
    }

    override suspend fun cambiarEstado(id: Int, estado: EstadoPedido): Int {
        return pedidoDao.cambiarEstado(id, estado.name)
    }

    override fun observarPedidosPorCliente(clienteId: Int): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorCliente(clienteId)
    }

    override fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorEstado(estado.name)
    }

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> {
        return pedidoDao.observarUno(id)
    }

    override fun observarVentasHoy(inicioMillis: Long): Flow<Double> {
        return pedidoDao.ventasDesde(inicioMillis)
    }

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> {
        return pedidoDao.cantidadPorEstado(estado.name)
    }
    override fun observarTodosLosPedidos(): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarTodosLosPedidos()
    }

    override suspend fun actualizarEstadoPedido(pedidoId: Int, nuevoEstado: String) {
        pedidoDao.actualizarEstadoPedido(pedidoId, nuevoEstado)
    }
    override fun observarTodosLosProductos(): Flow<List<Producto>> {
        return productoDao.observarTodosLosProductos()
    }

    override suspend fun guardarProducto(producto: Producto) {
        if (producto.id == 0) {
            productoDao.insertarProducto(producto)
        } else {
            productoDao.actualizarProducto(producto)
        }
    }

    override suspend fun eliminarProducto(producto: Producto) {
        productoDao.eliminarProducto(producto)
    }

    override suspend fun alternarDisponibilidad(producto: Producto) {
        productoDao.actualizarDisponible(producto.id, !producto.disponible)
    }
}
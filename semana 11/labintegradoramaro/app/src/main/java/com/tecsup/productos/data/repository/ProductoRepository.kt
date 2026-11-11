package com.tecsup.productos.data.repository

import com.tecsup.productos.data.local.CarritoEntity
import com.tecsup.productos.data.local.HistorialEntity
import com.tecsup.productos.data.local.PedidoEntity
import com.tecsup.productos.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    val items: Flow<List<Producto>>
    val favoritos: Flow<List<Producto>>
    val carrito: Flow<List<CarritoEntity>>
    val totalCarrito: Flow<Double?>
    val historialReciente: Flow<List<Producto>>
    val historial: Flow<List<HistorialEntity>>
    val todosLosPedidos: Flow<List<PedidoEntity>>

    fun obtenerPedidosUsuario(email: String): Flow<List<PedidoEntity>>
    suspend fun guardarPedido(pedido: PedidoEntity)

    suspend fun refrescar(): Result<Int>
    suspend fun obtenerDetalle(id: Int): Result<Producto>
    suspend fun alternarFavorito(id: Int)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()

    suspend fun agregarAlCarrito(producto: Producto)
    suspend fun incrementarCantidadCarrito(id: Int, cantidadExtra: Int = 1)
    suspend fun eliminarDelCarrito(id: Int)
    suspend fun vaciarCarrito()

    suspend fun registrarVista(idProducto: Int)
    suspend fun limpiarHistorial()
}
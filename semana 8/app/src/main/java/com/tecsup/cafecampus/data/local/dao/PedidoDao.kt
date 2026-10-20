package com.tecsup.cafecampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PedidoDao {

    @Insert
    abstract suspend fun insertarPedido(pedido: Pedido): Long

    @Insert
    abstract suspend fun insertarDetalles(detalles: List<DetallePedido>): List<Long>

    @Transaction
    open suspend fun crearPedido(pedido: Pedido, detalles: List<DetallePedido>): Long {
        val pedidoId = insertarPedido(pedido)
        val detallesConId = detalles.map { it.copy(pedidoId = pedidoId.toInt()) }
        insertarDetalles(detallesConId)
        return pedidoId
    }

    @Query("UPDATE pedidos SET estado = :estado WHERE id = :id")
    abstract suspend fun cambiarEstado(id: Int, estado: String): Int

    @Transaction
    @Query("SELECT * FROM pedidos WHERE clienteId = :clienteId ORDER BY fecha DESC")
    abstract fun observarPorCliente(clienteId: Int): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE estado = :estado ORDER BY fecha DESC")
    abstract fun observarPorEstado(estado: String): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :id LIMIT 1")
    abstract fun observarUno(id: Int): Flow<PedidoConDetalle?>

    @Query("SELECT COALESCE(SUM(total), 0.0) FROM pedidos WHERE fecha >= :inicioMillis AND estado != 'CANCELADO'")
    abstract fun ventasDesde(inicioMillis: Long): Flow<Double>

    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = :estado")
    abstract fun cantidadPorEstado(estado: String): Flow<Int>

    @Transaction
    @Query("SELECT * FROM pedidos ORDER BY id DESC")
    abstract fun observarTodosLosPedidos(): Flow<List<PedidoConDetalle>>

    @Query("UPDATE pedidos SET estado = :nuevoEstado WHERE id = :pedidoId")
    abstract suspend fun actualizarEstadoPedido(pedidoId: Int, nuevoEstado: String)
}
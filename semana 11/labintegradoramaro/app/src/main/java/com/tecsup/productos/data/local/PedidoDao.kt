package com.tecsup.productos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPedido(pedido: PedidoEntity)

    @Query("SELECT * FROM pedidos WHERE emailUsuario = :email ORDER BY id DESC")
    fun obtenerPedidosPorUsuario(email: String): Flow<List<PedidoEntity>>

    @Query("SELECT * FROM pedidos ORDER BY id DESC")
    fun obtenerTodosLosPedidos(): Flow<List<PedidoEntity>>
}
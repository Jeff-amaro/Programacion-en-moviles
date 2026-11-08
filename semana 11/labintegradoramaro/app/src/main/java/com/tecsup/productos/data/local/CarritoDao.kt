package com.tecsup.productos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {
    @Query("SELECT * FROM carrito")
    fun obtenerCarrito(): Flow<List<CarritoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: CarritoEntity)

    @Query("SELECT * FROM carrito WHERE idProducto = :id")
    suspend fun obtenerPorId(id: Int): CarritoEntity?

    @Query("UPDATE carrito SET cantidad = cantidad + :cantidadExtra WHERE idProducto = :id")
    suspend fun incrementarCantidad(id: Int, cantidadExtra: Int = 1)

    @Query("DELETE FROM carrito WHERE idProducto = :id")
    suspend fun eliminarItem(id: Int)

    @Query("DELETE FROM carrito")
    suspend fun vaciarCarrito()

    @Query("SELECT SUM(precio * cantidad) FROM carrito")
    fun obtenerTotal(): Flow<Double?>
}
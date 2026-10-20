package com.tecsup.cafecampus.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tecsup.cafecampus.data.local.entity.Producto
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProductoDao {

    @Query("SELECT * FROM productos ORDER BY id ASC")
    abstract fun observarTodosLosProductos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos ORDER BY id ASC")
    abstract fun obtenerTodos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    abstract fun observarPorId(id: Int): Flow<Producto?>

    @Query("SELECT * FROM productos WHERE categoria = :categoria ORDER BY id ASC")
    abstract fun observarPorCategoria(categoria: String): Flow<List<Producto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertar(producto: Producto): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarProducto(producto: Producto): Long

    @Update
    abstract suspend fun actualizarProducto(producto: Producto)

    @Delete
    abstract suspend fun eliminarProducto(producto: Producto)

    @Query("UPDATE productos SET disponible = :disponible WHERE id = :id")
    abstract suspend fun actualizarDisponible(id: Int, disponible: Boolean)
}
package com.tecsup.productos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos")
    fun obtenerTodos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE esFavorito = 1")
    fun obtenerFavoritos(): Flow<List<ProductoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(productos: List<ProductoEntity>)

    @Update
    suspend fun actualizar(producto: ProductoEntity)

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ProductoEntity?

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun contarProductos(): Int

    @Query("SELECT COUNT(*) FROM productos WHERE esFavorito = 1")
    suspend fun contarFavoritos(): Int

    @Query("DELETE FROM productos WHERE esFavorito = 0")
    suspend fun limpiarNoFavoritos()
}
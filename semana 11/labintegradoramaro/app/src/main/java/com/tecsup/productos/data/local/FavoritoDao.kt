package com.tecsup.productos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {

    @Query("SELECT * FROM favoritos WHERE userId = :userId")
    fun obtenerFavoritosPorUsuario(userId: String): Flow<List<FavoritoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarFavorito(favorito: FavoritoEntity)

    @Query("DELETE FROM favoritos WHERE productoId = :productoId AND userId = :userId")
    suspend fun eliminarFavorito(productoId: Int, userId: String)

    @Query("SELECT COUNT(*) FROM favoritos WHERE productoId = :productoId AND userId = :userId")
    suspend fun existeFavorito(productoId: Int, userId: String): Int
}
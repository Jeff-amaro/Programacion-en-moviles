package com.tecsup.productos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: HistorialEntity)

    @Query("SELECT * FROM historial ORDER BY timestamp DESC LIMIT 5")
    fun obtenerHistorialReciente(): Flow<List<HistorialEntity>>

    @Query("SELECT * FROM historial")
    fun obtenerHistorial(): Flow<List<HistorialEntity>>

    @Query("DELETE FROM historial")
    suspend fun limpiarHistorial()
}
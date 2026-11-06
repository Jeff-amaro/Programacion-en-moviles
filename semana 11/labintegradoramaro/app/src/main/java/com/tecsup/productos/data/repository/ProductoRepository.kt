package com.tecsup.productos.data.repository

import com.tecsup.productos.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    val items: Flow<List<Producto>> // sale de Room
    val favoritos: Flow<List<Producto>>

    suspend fun refrescar(): Result<Int> // Retrofit -> Room
    suspend fun obtenerDetalle(id: Int): Result<Producto>
    suspend fun alternarFavorito(id: Int)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
}
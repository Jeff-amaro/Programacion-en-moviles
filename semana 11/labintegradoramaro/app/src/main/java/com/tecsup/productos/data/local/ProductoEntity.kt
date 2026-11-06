package com.tecsup.productos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String,
    val esFavorito: Boolean
)
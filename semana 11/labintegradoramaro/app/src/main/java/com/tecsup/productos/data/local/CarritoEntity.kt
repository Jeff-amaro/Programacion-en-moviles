package com.tecsup.productos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito")
data class CarritoEntity(
    @PrimaryKey
    val idProducto: Int,
    val titulo: String,
    val precio: Double,
    val imagenUrl: String,
    val cantidad: Int
)
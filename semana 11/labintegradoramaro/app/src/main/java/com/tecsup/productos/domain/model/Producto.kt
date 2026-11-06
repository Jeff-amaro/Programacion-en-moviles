package com.tecsup.productos.domain.model

data class Producto(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String,
    val esFavorito: Boolean = false
)
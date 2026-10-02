package com.amaro.lab04carritotecsup.data

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val esFavorito: Boolean = false
)
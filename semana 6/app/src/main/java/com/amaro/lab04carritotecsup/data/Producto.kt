package com.amaro.lab04carritotecsup.data

data class Producto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    var esFavorito: Boolean = false
)
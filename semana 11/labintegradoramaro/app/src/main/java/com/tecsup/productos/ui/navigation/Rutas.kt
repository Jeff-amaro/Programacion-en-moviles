package com.tecsup.productos.ui.navigation

sealed class Rutas(val ruta: String) {
    data object Login : Rutas("login")
    data object Admin : Rutas("admin")
    data object Lista : Rutas("lista")
    data object Carrito : Rutas("carrito")

    data object Detalle : Rutas("detalle/{id}") {
        fun crearRuta(id: Int) = "detalle/$id"
    }
}
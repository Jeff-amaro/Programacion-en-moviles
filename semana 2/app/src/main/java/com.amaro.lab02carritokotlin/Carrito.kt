package com.amaro.lab02carritokotlin

// --- 1. ENCAPSULAMIENTO Y ABSTRACCIÓN ---

// Abstracción: Interfaz que define la capacidad de calcular costo total
interface Calculable {
    fun calcularSubtotal(): Double
}

// Encapsulamiento: Producto con propiedades protegidas y métodos para manipular estado
open class Producto(
    val nombre: String,
    val precio: Double,
    cantidadInicial: Int
) : Calculable {

    var cantidad: Int = cantidadInicial
        private set // La cantidad solo se puede modificar mediante métodos controlados

    fun agregarCantidad(adicional: Int) {
        if (adicional > 0) {
            this.cantidad += adicional
        }
    }

    override fun calcularSubtotal(): Double {
        return precio * cantidad
    }
}
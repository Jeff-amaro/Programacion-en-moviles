package com.amaro.lab02carritokotlin

interface Calculable {
    fun calcularSubtotal(): Double
}

open class Producto(
    val nombre: String,
    val precio: Double,
    cantidadInicial: Int
) : Calculable {

    var cantidad: Int = cantidadInicial
        private set

    fun agregarCantidad(adicional: Int) {
        if (adicional > 0) {
            this.cantidad += adicional
        }
    }

    override fun calcularSubtotal(): Double {
        return precio * cantidad
    }
}

class ProductoFisico(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val pesoKg: Double
) : Producto(nombre, precio, cantidad) {

    fun calcularCostoEnvio(): Double {
        return pesoKg * 5.0
    }
}

class ProductoDigital(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val urlDescarga: String
) : Producto(nombre, precio, cantidad)
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

interface EstrategiaDescuento {
    fun calcularDescuento(montoTotal: Double): Double
    fun obtenerNombre(): String
}

class DescuentoPorMonto : EstrategiaDescuento {
    override fun calcularDescuento(montoTotal: Double): Double {
        return when {
            montoTotal > 5000 -> montoTotal * 0.10
            montoTotal > 3000 -> montoTotal * 0.05
            else -> 0.0
        }
    }

    override fun obtenerNombre(): String {
        return "Descuento por Monto Elevado"
    }
}

class SinDescuento : EstrategiaDescuento {
    override fun calcularDescuento(montoTotal: Double): Double = 0.0
    override fun obtenerNombre(): String = "Sin Descuento"
}
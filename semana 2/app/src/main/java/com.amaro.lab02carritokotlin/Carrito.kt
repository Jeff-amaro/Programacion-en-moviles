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

class CarritoDeCompras(
    val cliente: String,
    private val estrategiaDescuento: EstrategiaDescuento = DescuentoPorMonto()
) {
    private val listaProductos = mutableListOf<Producto>()

    fun agregarProducto(productoNuevo: Producto) {
        val existente = listaProductos.find { it.nombre.equals(productoNuevo.nombre, ignoreCase = true) }
        if (existente != null) {
            existente.agregarCantidad(productoNuevo.cantidad)
        } else {
            listaProductos.add(productoNuevo)
        }
    }

    fun obtenerSubtotal(): Double = listaProductos.sumOf { it.calcularSubtotal() }
    fun obtenerIGV(): Double = obtenerSubtotal() * 0.18
    fun obtenerTotalSinDescuento(): Double = obtenerSubtotal() + obtenerIGV()
    fun obtenerMontoDescuento(): Double = estrategiaDescuento.calcularDescuento(obtenerTotalSinDescuento())
    fun obtenerTotalFinal(): Double = obtenerTotalSinDescuento() - obtenerMontoDescuento()
    fun obtenerProductoMasCaro(): Producto? = listaProductos.maxByOrNull { it.precio }
    fun obtenerTotalProductos(): Int = listaProductos.sumOf { it.cantidad }
    fun mostrarResumen() {
        println("=========================================")
        println("   CARRITO DE COMPRAS - TIENDA TECSUP    ")
        println("=========================================")
        println("Cliente: $cliente")
        println()

        println("--------- DETALLE DEL CARRITO ---------")
        listaProductos.forEachIndexed { index, p ->
            val tipo = if (p is ProductoFisico) "[Físico]" else "[Digital]"
            println(String.format("%d. %-20s %-9s x%d  S/ %8.2f", index + 1, p.nombre, tipo, p.cantidad, p.calcularSubtotal()))
        }
        println("---------------------------------------")

        println("Cantidad total de artículos: ${obtenerTotalProductos()}")
        obtenerProductoMasCaro()?.let {
            println("Producto más caro: ${it.nombre} (S/ %.2f)".format(it.precio))
        }
        println()

        val subtotal = obtenerSubtotal()
        val igv = obtenerIGV()
        val totalSinDesc = obtenerTotalSinDescuento()
        val descuento = obtenerMontoDescuento()

        println(String.format("%-25s S/ %8.2f", "Subtotal :", subtotal))
        println(String.format("%-25s S/ %8.2f", "IGV (18%) :", igv))
        println(String.format("%-25s S/ %8.2f", "TOTAL :", totalSinDesc))

        if (descuento > 0.0) {
            println(String.format("%-25s S/ %8.2f", "Descuento (${estrategiaDescuento.obtenerNombre()}) :", descuento))
            println(String.format("%-25s S/ %8.2f", "TOTAL CON DESCUENTO :", obtenerTotalFinal()))
        }
    }
}
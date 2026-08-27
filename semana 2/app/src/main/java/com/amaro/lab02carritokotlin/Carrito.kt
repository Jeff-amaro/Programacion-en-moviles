package com.amaro.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}
fun mostrarDetalle(productos: List<Producto>) {
    println("--------- DETALLE DEL CARRITO ---------")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%d  S/ %8.2f", i, p.nombre, p.cantidad, importe))
        i++
    }
    println("---------------------------------------")
}

fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP    ")
    println("=========================================")

    val nombreCliente = "Jeff Amaro"
    val carrito = mutableListOf<Producto>()
    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Teclado Mecánico", 120.0, 1))
    carrito.add(Producto("Monitor 24''", 650.0, 1))
    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Audifonos Sony", 120.0, 1))

    mostrarDetalle(carrito)

    println("Cantidad de productos: ${carrito.size}")
    println()

    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " + String.format("(S/ %.2f)", masCaro.precio))
    }
    println()

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val totalSinDescuento = calcularTotal(subtotal, igv)
    val descuento = calcularDescuento(totalSinDescuento)
    val totalFinal = totalSinDescuento - descuento


    println(String.format("%-25s S/ %8.2f", "Subtotal :", subtotal))
    println(String.format("%-25s S/ %8.2f", "IGV (18%) :", igv))
    println(String.format("%-25s S/ %8.2f", "TOTAL :", totalSinDescuento))

    if (descuento > 0.0) {
        val pct = if (totalSinDescuento > 5000) "10%" else "5%"
        println(String.format("%-25s S/ %8.2f", "Descuento ($pct) :", descuento))
        println(String.format("%-25s S/ %8.2f", "TOTAL CON DESCUENTO :", totalFinal))
    }
}


import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    println("LIBRERIA")

    print("Nombre del producto:")
    val nombreProducto = scanner.nextLine()

    print("Precio del producto:")
    val precio = scanner.nextDouble()

    print("Cantidad:")
    val cantidad = scanner.nextInt()

    println("Datos registrados:")
    println("Producto: $nombreProducto | Precio: S/ $precio | Cantidad: $cantidad")

    val subtotal = precio * cantidad
    println("\nSubtotal a financiar: S/ $subtotal")

    var cuotas: Int
    var porcentajeInteres: Double

    do {
        println("\n  Opciones de financiamiento  ")
        println(" 6 cuotas (20% interes)")
        println(" 12 cuotas (40% interes)")
        println(" 24 cuotas (60% interes)")
        println("Ingrese las cantidad de cuotas: ")
        cuotas = scanner.nextInt()

        porcentajeInteres = when (cuotas) {
            6 -> 0.20
            12 -> 0.40
            24 -> 0.60
            else -> {
                println("opcion invalida, ingrese un numero correcto")
                0.0
            }
        }
    } while (porcentajeInteres == 0.0)
    val montoInteres = subtotal * porcentajeInteres
    val totalPagar = subtotal + montoInteres
    val pagoMensual = totalPagar / cuotas

    println("\n  Boleta del Credito: ")
    println("Interes aplicado: ${(porcentajeInteres * 100).toInt()}%(S/ $montoInteres)")
    println("Total a pagar: S/$totalPagar")
    println("Pago mensual: S/$pagoMensual")

    println("\n  Cronograma de pagos  ")

    var fechaPago = LocalDate.now()
    val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    for(i in 1..cuotas){
        fechaPago = fechaPago.plusMonths(1)
        println("Cuotas $i \t Fecha: ${fechaPago.format(formato)} \t Monto: S/ ${String.format("%.2f",pagoMensual)}")
    }
}
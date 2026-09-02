import java.util.Scanner

fun main(){
    val scanner = Scanner(System.`in`)
    println("LIBRERIA")

    print("Nombre del producto:")
    val nombreProducto = scanner.nextLine()

    print("Precio del producto:")
    val precio = scanner.nextDouble()

    print("Cantidad:")
    val cantidad = scanner.nextInt()

    println("Datos registrados:")
    println("Producto: $nombreProducto  Precio: S/ $precio | Cantidad: $cantidad")
}
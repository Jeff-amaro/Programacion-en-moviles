package com.tecsup.cafecampus.data.local

import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario

object DatosIniciales {
    suspend fun sembrar(db: CafeDatabase) {
        if (db.usuarioDao().contar() == 0) {
            val usuarios = listOf(
                Usuario(
                    nombre = "Admin",
                    correo = "admin@cafecampus.pe",
                    clave = "123456",
                    rol = "ADMIN"
                ),
                Usuario(
                    nombre = "Juan Pérez",
                    correo = "juan@cafecampus.pe",
                    clave = "123456",
                    rol = "CLIENTE"
                )
            )
            usuarios.forEach { db.usuarioDao().insertar(it) }

            val productos = listOf(
                Producto(
                    nombre = "Café Americano",
                    descripcion = "Espresso con agua caliente",
                    categoria = "Bebidas Calientes",
                    precio = 6.50,
                    disponible = true,
                    icono = "local_cafe"
                ),
                Producto(
                    nombre = "Cappuccino",
                    descripcion = "Espresso, leche al vapor y espuma",
                    categoria = "Bebidas Calientes",
                    precio = 8.50,
                    disponible = true,
                    icono = "local_cafe"
                ),
                Producto(
                    nombre = "Latte",
                    descripcion = "Espresso suave con abundante leche al vapor",
                    categoria = "Bebidas Calientes",
                    precio = 8.00,
                    disponible = true,
                    icono = "local_cafe"
                ),
                Producto(
                    nombre = "Sandwich de Pollo",
                    descripcion = "Pan integral, pechuga deshilachada y palta",
                    categoria = "Snacks",
                    precio = 12.00,
                    disponible = true,
                    icono = "fastfood"
                ),
                Producto(
                    nombre = "Empanada de Carne",
                    descripcion = "Empanada horneada rellena de carne picada",
                    categoria = "Snacks",
                    precio = 7.00,
                    disponible = true,
                    icono = "fastfood"
                ),
                Producto(
                    nombre = "Torta de Chocolate",
                    descripcion = "Porción de pastel de chocolate artesanal",
                    categoria = "Postres",
                    precio = 9.50,
                    disponible = true,
                    icono = "cake"
                )
            )
            productos.forEach { db.productoDao().insertar(it) }
        }
    }
}
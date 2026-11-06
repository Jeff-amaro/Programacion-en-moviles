package com.tecsup.productos.mapper

import com.tecsup.productos.data.local.ProductoEntity
import com.tecsup.productos.data.remote.dto.ProductoDto
import com.tecsup.productos.domain.model.Producto

fun ProductoDto.aModelo(): Producto {
    return Producto(
        id = id,
        titulo = title,
        descripcion = description,
        precio = price,
        imagenUrl = thumbnail,
        esFavorito = false
    )
}

fun ProductoEntity.aModelo(): Producto {
    return Producto(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        precio = precio,
        imagenUrl = imagenUrl,
        esFavorito = esFavorito
    )
}

fun Producto.aEntity(): ProductoEntity {
    return ProductoEntity(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        precio = precio,
        imagenUrl = imagenUrl,
        esFavorito = esFavorito
    )
}
package com.tecsup.productos.mapper

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
package com.tecsup.productos.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductoResponseDto(
    val products: List<ProductoDto>
)

@Serializable
data class ProductoDto(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String
)
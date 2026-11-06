package com.tecsup.productos.data.remote

import com.tecsup.productos.data.remote.dto.ProductoDto
import com.tecsup.productos.data.remote.dto.ProductoResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products/category/{categoria}")
    suspend fun listar(@Path("categoria") categoria: String = "smartphones"): ProductoResponseDto

    @GET("products/{id}")
    suspend fun obtenerDetalle(@Path("id") id: Int): ProductoDto
}
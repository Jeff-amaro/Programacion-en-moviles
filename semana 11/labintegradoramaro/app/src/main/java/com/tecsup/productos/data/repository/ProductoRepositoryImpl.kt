package com.tecsup.productos.data.repository

import com.tecsup.productos.data.remote.ApiService
import com.tecsup.productos.domain.model.Producto
import com.tecsup.productos.mapper.aModelo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import java.io.IOException

class ProductoRepositoryImpl(
    private val api: ApiService
) : ProductoRepository {

    private val _items = MutableStateFlow<List<Producto>>(emptyList())
    override val items: Flow<List<Producto>> = _items.asStateFlow()

    override val favoritos: Flow<List<Producto>> = MutableStateFlow(emptyList())

    override suspend fun refrescar(): Result<Int> = try {
        val respuesta = api.listar("smartphones")
        val lista = respuesta.products.map { it.aModelo() }
        _items.value = lista
        Result.success(lista.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Producto> = try {
        val dto = api.obtenerDetalle(id)
        Result.success(dto.aModelo())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) {}
    override suspend fun cantidadGuardada(): Int = _items.value.size
    override suspend fun cantidadFavoritos(): Int = 0
    override suspend fun limpiarCache() { _items.value = emptyList() }
}
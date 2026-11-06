package com.tecsup.productos.data.repository

import com.tecsup.productos.data.local.ProductoDao
import com.tecsup.productos.data.remote.ApiService
import com.tecsup.productos.domain.model.Producto
import com.tecsup.productos.mapper.aEntity
import com.tecsup.productos.mapper.aModelo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ProductoRepositoryImpl(
    private val api: ApiService,
    private val dao: ProductoDao
) : ProductoRepository {

    override val items: Flow<List<Producto>> = dao.obtenerTodos().map { lista ->
        lista.map { it.aModelo() }
    }

    override val favoritos: Flow<List<Producto>> = dao.obtenerFavoritos().map { lista ->
        lista.map { it.aModelo() }
    }

    override suspend fun refrescar(): Result<Int> = try {
        val respuesta = api.listar("smartphones")
        val entidades = respuesta.products.map { it.aModelo().aEntity() }

        dao.insertar(entidades)

        Result.success(entidades.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Producto> = try {
        val local = dao.obtenerPorId(id)
        if (local != null) {
            Result.success(local.aModelo())
        } else {
            val dto = api.obtenerDetalle(id)
            val modelo = dto.aModelo()
            dao.actualizar(modelo.aEntity())
            Result.success(modelo)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) {
        val local = dao.obtenerPorId(id)
        if (local != null) {
            val actualizado = local.copy(esFavorito = !local.esFavorito)
            dao.actualizar(actualizado)
        }
    }

    override suspend fun cantidadGuardada(): Int = dao.contarProductos()
    override suspend fun cantidadFavoritos(): Int = dao.contarFavoritos()
    override suspend fun limpiarCache() {
        dao.limpiarNoFavoritos()
    }
}
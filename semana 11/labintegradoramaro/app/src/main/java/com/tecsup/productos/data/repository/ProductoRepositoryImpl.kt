package com.tecsup.productos.data.repository

import com.tecsup.productos.data.local.CarritoDao
import com.tecsup.productos.data.local.CarritoEntity
import com.tecsup.productos.data.local.HistorialDao
import com.tecsup.productos.data.local.HistorialEntity
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
    private val dao: ProductoDao,
    private val carritoDao: CarritoDao,
    private val historialDao: HistorialDao
) : ProductoRepository {

    override val items: Flow<List<Producto>> = dao.obtenerTodos().map { lista ->
        lista.map { it.aModelo() }
    }

    override val favoritos: Flow<List<Producto>> = dao.obtenerFavoritos().map { lista ->
        lista.map { it.aModelo() }
    }

    override val carrito: Flow<List<CarritoEntity>> = carritoDao.obtenerCarrito()
    override val totalCarrito: Flow<Double?> = carritoDao.obtenerTotal()

    override val historialReciente: Flow<List<Producto>> = historialDao.obtenerHistorialReciente().map { historialList ->
        historialList.mapNotNull { historialEntity ->
            dao.obtenerPorId(historialEntity.idProducto)?.aModelo()
        }
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

    override suspend fun agregarAlCarrito(producto: Producto) {
        val existente = carritoDao.obtenerPorId(producto.id)
        if (existente != null) {
            carritoDao.incrementarCantidad(producto.id)
        } else {
            carritoDao.insertar(
                CarritoEntity(
                    idProducto = producto.id,
                    titulo = producto.titulo,
                    precio = producto.precio,
                    imagenUrl = producto.imagenUrl,
                    cantidad = 1
                )
            )
        }
    }

    override suspend fun incrementarCantidadCarrito(id: Int, cantidadExtra: Int) {
        carritoDao.incrementarCantidad(id, cantidadExtra)
    }

    override suspend fun eliminarDelCarrito(id: Int) {
        carritoDao.eliminarItem(id)
    }

    override suspend fun vaciarCarrito() {
        carritoDao.vaciarCarrito()
    }

    override suspend fun registrarVista(idProducto: Int) {
        historialDao.insertar(HistorialEntity(idProducto, System.currentTimeMillis()))
    }
}
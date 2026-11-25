
package com.amaro.murocampus.data

import com.amaro.murocampus.data.remote.PostApi
import com.amaro.murocampus.data.local.PostDao
import com.amaro.murocampus.data.local.CambioPostEntity
import com.amaro.murocampus.data.local.toEntity
import com.amaro.murocampus.data.local.toPost
import com.amaro.murocampus.model.Post
import com.amaro.murocampus.model.Comentario
import com.amaro.murocampus.model.NuevoPost
import com.amaro.murocampus.model.EditarPost
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

data class CambioLocal(
    val id: Int,
    val titulo: String?,
    val eliminado: Boolean
)

interface PostRepository {

    val usandoCache: StateFlow<Boolean>

    suspend fun listar(): Result<List<Post>>

    suspend fun detalle(id: Int): Result<Post>

    suspend fun comentarios(id: Int): Result<List<Comentario>>

    suspend fun porUsuario(userId: Int): Result<List<Post>>

    suspend fun crear(nuevo: NuevoPost): Result<Post>

    suspend fun eliminar(id: Int): Result<Unit>

    suspend fun editar(
        id: Int,
        cambios: EditarPost
    ): Result<Post>

    suspend fun listarPaginado(
        inicio: Int,
        limite: Int,
        userId: Int? = null
    ): Result<List<Post>>

    suspend fun cambiosLocales(): Result<List<CambioLocal>>
}

class PostRepositoryRemote(
    private val api: PostApi,
    private val dao: PostDao
) : PostRepository {

    private val _usandoCache = MutableStateFlow(false)

    override val usandoCache =
        _usandoCache.asStateFlow()

    private suspend fun <T> llamar(
        bloque: suspend () -> T
    ): Result<T> {
        return try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // MEJORA 6: Consultar internet o Room
    private suspend fun consultarConCache(
        clave: String,
        bloque: suspend () -> List<Post>
    ): Result<List<Post>> = llamar {

        val posts = try {

            bloque()

        } catch (e: IOException) {

            val guardados = dao.leerPagina(clave)
                ?: throw e

            _usandoCache.value = true

            return@llamar guardados.map {
                it.toPost()
            }
        }

        dao.guardarPagina(
            clave,
            posts.mapIndexed { posicion, post ->
                post.toEntity(clave, posicion)
            }
        )

        _usandoCache.value = false

        posts
    }

    override suspend fun listar(): Result<List<Post>> =
        consultarConCache("todos") {
            api.listar()
        }

    override suspend fun detalle(id: Int): Result<Post> =
        llamar { api.detalle(id) }

    override suspend fun comentarios(
        id: Int
    ): Result<List<Comentario>> =
        llamar { api.comentarios(id) }

    override suspend fun porUsuario(
        userId: Int
    ): Result<List<Post>> =
        consultarConCache("usuario:$userId") {
            api.porUsuario(userId)
        }

    override suspend fun crear(
        nuevo: NuevoPost
    ): Result<Post> =
        llamar { api.crear(nuevo) }

    override suspend fun eliminar(id: Int): Result<Unit> =
        llamar {

            val respuesta = api.eliminar(id)

            if (!respuesta.isSuccessful) {
                throw HttpException(respuesta)
            }

            // Guardar la eliminación localmente
            dao.guardarCambio(
                CambioPostEntity(
                    id = id,
                    eliminado = true
                )
            )

            Unit
        }

    override suspend fun editar(
        id: Int,
        cambios: EditarPost
    ): Result<Post> =
        llamar {

            val postEditado = api.editar(id, cambios)

            // Guardar la edición localmente
            dao.guardarCambio(
                CambioPostEntity(
                    id = id,
                    titulo = postEditado.title
                )
            )

            postEditado
        }

    override suspend fun listarPaginado(
        inicio: Int,
        limite: Int,
        userId: Int?
    ): Result<List<Post>> {

        val clave =
            "${userId ?: "todos"}:$inicio:$limite"

        return consultarConCache(clave) {
            api.listarPaginado(
                inicio,
                limite,
                userId
            )
        }
    }

    override suspend fun cambiosLocales():
            Result<List<CambioLocal>> =
        llamar {

            dao.obtenerCambios().map {
                CambioLocal(
                    id = it.id,
                    titulo = it.titulo,
                    eliminado = it.eliminado
                )
            }
        }
}

fun mensajeDe(e: Throwable): String = when (e) {

    is IOException ->
        "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."

    is HttpException ->
        "Error del servidor (${e.code()})"

    is SerializationException ->
        "Respuesta inesperada"

    else ->
        "Error desconocido"
}

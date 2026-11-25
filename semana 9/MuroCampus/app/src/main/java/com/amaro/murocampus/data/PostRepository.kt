
package com.amaro.murocampus.data

import com.amaro.murocampus.data.remote.PostApi
import com.amaro.murocampus.model.Post
import com.amaro.murocampus.model.Comentario
import com.amaro.murocampus.model.NuevoPost
import com.amaro.murocampus.model.EditarPost
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

interface PostRepository {

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
}

class PostRepositoryRemote(
    private val api: PostApi
) : PostRepository {

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

    override suspend fun listar(): Result<List<Post>> =
        llamar { api.listar() }

    override suspend fun detalle(id: Int): Result<Post> =
        llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> =
        llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> =
        llamar { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> =
        llamar { api.crear(nuevo) }

    override suspend fun eliminar(id: Int): Result<Unit> =
        llamar {
            val respuesta = api.eliminar(id)

            if (!respuesta.isSuccessful) {
                throw HttpException(respuesta)
            }

            Unit
        }

    // MEJORA 4: Editar publicación
    override suspend fun editar(
        id: Int,
        cambios: EditarPost
    ): Result<Post> =
        llamar { api.editar(id, cambios) }
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

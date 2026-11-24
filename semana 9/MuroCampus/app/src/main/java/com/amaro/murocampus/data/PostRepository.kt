package com.amaro.murocampus.data

import com.amaro.murocampus.data.remote.PostApi
import com.amaro.murocampus.model.Post
import com.amaro.murocampus.model.Comentario
import com.amaro.murocampus.model.NuevoPost
import kotlinx.coroutines.CancellationException

interface PostRepository {

    suspend fun listar(): Result<List<Post>>

    suspend fun detalle(id: Int): Result<Post>

    suspend fun comentarios(id: Int): Result<List<Comentario>>

    suspend fun porUsuario(userId: Int): Result<List<Post>>

    suspend fun crear(nuevo: NuevoPost): Result<Post>
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
}

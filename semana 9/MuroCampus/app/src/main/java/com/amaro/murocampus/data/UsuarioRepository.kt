package com.amaro.murocampus.data

import com.amaro.murocampus.data.remote.UsuarioApi
import com.amaro.murocampus.model.Usuario
import java.util.concurrent.CancellationException

interface UsuarioRepository {

    suspend fun listar(): Result<List<Usuario>>
}

class UsuarioRepositoryRemote(
    private val api: UsuarioApi
) : UsuarioRepository {

    override suspend fun listar(): Result<List<Usuario>> =
        try {
            Result.success(api.listar())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}

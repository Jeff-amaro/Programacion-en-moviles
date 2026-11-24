package com.amaro.murocampus.data.remote

import com.amaro.murocampus.model.Usuario
import retrofit2.http.GET

interface UsuarioApi {

    @GET("users")
    suspend fun listar(): List<Usuario>
}

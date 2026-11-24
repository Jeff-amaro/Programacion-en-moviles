package com.amaro.murocampus.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

// TODO: Crear modelo Post

// TODO: Crear modelo Comentario

// TODO: Crear modelo NuevoPost

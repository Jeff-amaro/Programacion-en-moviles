
package com.amaro.murocampus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amaro.murocampus.model.Post

@Entity(
    tableName = "publicaciones",
    primaryKeys = ["pagina", "id"]
)
data class PostEntity(
    val pagina: String,
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val posicion: Int
)

// Registra las páginas descargadas
@Entity(tableName = "paginas")
data class PaginaEntity(
    @PrimaryKey val clave: String
)

// Conserva las ediciones y eliminaciones locales
@Entity(tableName = "cambios")
data class CambioPostEntity(
    @PrimaryKey val id: Int,
    val titulo: String? = null,
    val eliminado: Boolean = false
)

// Convertir modelo de la API a entidad Room
fun Post.toEntity(
    pagina: String,
    posicion: Int
): PostEntity = PostEntity(
    pagina = pagina,
    id = id,
    userId = userId,
    title = title,
    body = body,
    posicion = posicion
)

// Convertir entidad Room a modelo de la aplicación
fun PostEntity.toPost(): Post = Post(
    userId = userId,
    id = id,
    title = title,
    body = body
)

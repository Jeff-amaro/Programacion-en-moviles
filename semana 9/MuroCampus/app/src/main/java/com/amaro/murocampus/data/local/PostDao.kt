
package com.amaro.murocampus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarPosts(
        posts: List<PostEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarPagina(
        pagina: PaginaEntity
    )

    @Query(
        "DELETE FROM publicaciones WHERE pagina = :clave"
    )
    abstract suspend fun borrarPagina(clave: String)

    @Query(
        "SELECT * FROM publicaciones " +
                "WHERE pagina = :clave ORDER BY posicion"
    )
    abstract suspend fun obtenerPosts(
        clave: String
    ): List<PostEntity>

    @Query(
        "SELECT EXISTS(SELECT 1 FROM paginas WHERE clave = :clave)"
    )
    abstract suspend fun existePagina(
        clave: String
    ): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun guardarCambio(
        cambio: CambioPostEntity
    )

    @Query("SELECT * FROM cambios")
    abstract suspend fun obtenerCambios(): List<CambioPostEntity>

    @Transaction
    open suspend fun guardarPagina(
        clave: String,
        posts: List<PostEntity>
    ) {
        borrarPagina(clave)

        if (posts.isNotEmpty()) {
            insertarPosts(posts)
        }

        insertarPagina(PaginaEntity(clave))
    }

    @Transaction
    open suspend fun leerPagina(
        clave: String
    ): List<PostEntity>? {

        if (!existePagina(clave)) {
            return null
        }

        return obtenerPosts(clave)
    }
}

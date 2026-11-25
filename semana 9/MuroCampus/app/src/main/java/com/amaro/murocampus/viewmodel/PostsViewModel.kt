
package com.amaro.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amaro.murocampus.data.PostRepository
import com.amaro.murocampus.model.Post
import com.amaro.murocampus.model.Comentario
import com.amaro.murocampus.model.NuevoPost
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

class PostsViewModel(
    private val repo: PostRepository
) : ViewModel() {

    private val _lista =
        MutableStateFlow<UiState<List<Post>>>(
            UiState.Cargando
        )

    val lista = _lista.asStateFlow()

    private val _detalle =
        MutableStateFlow<UiState<DetalleUi>>(
            UiState.Cargando
        )

    val detalle = _detalle.asStateFlow()

    private val _usuarioFiltro =
        MutableStateFlow<Int?>(null)

    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    // Estado de la nueva publicación
    private val _publicacion =
        MutableStateFlow<UiState<Post>?>(null)

    val publicacion = _publicacion.asStateFlow()

    private var detalleJob: Job? = null
    private var listaJob: Job? = null
    private var publicarJob: Job? = null

    init {
        cargar()
    }

    fun cargar() {

        listaJob?.cancel()

        listaJob = viewModelScope.launch {

            _lista.value = UiState.Cargando

            val userId = _usuarioFiltro.value

            val resultado = if (userId == null) {
                repo.listar()
            } else {
                repo.porUsuario(userId)
            }

            resultado
                .onSuccess { posts ->
                    _lista.value = UiState.Exito(posts)
                }
                .onFailure { error ->
                    _lista.value = UiState.Error(
                        mensajeDe(error)
                    )
                }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun abrir(id: Int) {

        detalleJob?.cancel()

        detalleJob = viewModelScope.launch {

            _detalle.value = UiState.Cargando

            repo.detalle(id)
                .onSuccess { post ->

                    repo.comentarios(id)
                        .onSuccess { comentarios ->
                            _detalle.value = UiState.Exito(
                                DetalleUi(post, comentarios)
                            )
                        }
                        .onFailure { error ->
                            _detalle.value = UiState.Error(
                                mensajeDe(error)
                            )
                        }
                }
                .onFailure { error ->
                    _detalle.value = UiState.Error(
                        mensajeDe(error)
                    )
                }
        }
    }

    fun cerrarDetalle() {
        detalleJob?.cancel()
    }

    // Crear una publicación
    fun publicar(titulo: String, texto: String) {

        if (titulo.isBlank() || texto.isBlank()) {
            return
        }

        if (_publicacion.value == UiState.Cargando) {
            return
        }

        _publicacion.value = UiState.Cargando

        publicarJob = viewModelScope.launch {

            val nuevo = NuevoPost(
                userId = 1,
                title = titulo.trim(),
                body = texto.trim()
            )

            repo.crear(nuevo)
                .onSuccess { post ->
                    _publicacion.value = UiState.Exito(post)
                }
                .onFailure { error ->
                    _publicacion.value = UiState.Error(
                        mensajeDe(error)
                    )
                }
        }
    }

    fun limpiarPublicacion() {
        publicarJob?.cancel()
        _publicacion.value = null
    }
}

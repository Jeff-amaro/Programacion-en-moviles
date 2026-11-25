
package com.amaro.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amaro.murocampus.data.PostRepository
import com.amaro.murocampus.model.Post
import com.amaro.murocampus.model.Comentario
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

    // Filtro por usuario
    private val _usuarioFiltro =
        MutableStateFlow<Int?>(null)

    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    private var detalleJob: Job? = null
    private var listaJob: Job? = null

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
                                DetalleUi(
                                    post = post,
                                    comentarios = comentarios
                                )
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
}

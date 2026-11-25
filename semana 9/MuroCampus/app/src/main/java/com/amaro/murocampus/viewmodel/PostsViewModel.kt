
package com.amaro.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amaro.murocampus.data.PostRepository
import com.amaro.murocampus.data.mensajeDe
import com.amaro.murocampus.model.Post
import com.amaro.murocampus.model.Comentario
import com.amaro.murocampus.model.NuevoPost
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
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

    private val _publicacion =
        MutableStateFlow<UiState<Post>?>(null)

    val publicacion = _publicacion.asStateFlow()

    private val _busqueda = MutableStateFlow("")

    val busqueda = _busqueda.asStateFlow()

    // MEJORA 3: Estado de eliminación
    private val _eliminacion =
        MutableStateFlow<UiState<Unit>?>(null)

    val eliminacion = _eliminacion.asStateFlow()

    private val eliminados = mutableSetOf<Int>()

    val listaFiltrada = combine(
        lista,
        busqueda
    ) { estado, texto ->

        when (estado) {

            is UiState.Exito -> {
                UiState.Exito(
                    estado.datos.filter { post ->
                        post.title.contains(
                            texto.trim(),
                            ignoreCase = true
                        )
                    }
                )
            }

            UiState.Cargando -> UiState.Cargando

            is UiState.Error -> estado
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = UiState.Cargando
    )

    private var detalleJob: Job? = null
    private var listaJob: Job? = null
    private var publicarJob: Job? = null
    private var eliminarJob: Job? = null

    init {
        cargar()
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
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
                    _lista.value = UiState.Exito(
                        posts.filterNot {
                            it.id in eliminados
                        }
                    )
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
        eliminarJob?.cancel()
        _eliminacion.value = null

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
        eliminarJob?.cancel()
        _eliminacion.value = null
    }

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

    // MEJORA 3: Eliminar publicación
    fun eliminar(id: Int) {

        if (_eliminacion.value == UiState.Cargando ||
            id in eliminados
        ) {
            return
        }

        _eliminacion.value = UiState.Cargando

        eliminarJob = viewModelScope.launch {

            repo.eliminar(id)
                .onSuccess {

                    eliminados.add(id)

                    val actual = _lista.value

                    if (actual is UiState.Exito) {

                        _lista.value = UiState.Exito(
                            actual.datos.filterNot {
                                it.id == id
                            }
                        )
                    }

                    _eliminacion.value =
                        UiState.Exito(Unit)
                }
                .onFailure { error ->

                    _eliminacion.value = UiState.Error(
                        mensajeDe(error)
                    )
                }
        }
    }
}

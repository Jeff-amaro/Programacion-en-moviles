
package com.amaro.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amaro.murocampus.data.PostRepository
import com.amaro.murocampus.data.mensajeDe
import com.amaro.murocampus.model.Post
import com.amaro.murocampus.model.Comentario
import com.amaro.murocampus.model.NuevoPost
import com.amaro.murocampus.model.EditarPost
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
        MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)

    val lista = _lista.asStateFlow()

    private val _detalle =
        MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)

    val detalle = _detalle.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)

    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    private val _publicacion =
        MutableStateFlow<UiState<Post>?>(null)

    val publicacion = _publicacion.asStateFlow()

    private val _busqueda = MutableStateFlow("")

    val busqueda = _busqueda.asStateFlow()

    private val _eliminacion =
        MutableStateFlow<UiState<Unit>?>(null)

    val eliminacion = _eliminacion.asStateFlow()

    // MEJORA 4: Estado de edición
    private val _edicion =
        MutableStateFlow<UiState<Post>?>(null)

    val edicion = _edicion.asStateFlow()

    private val eliminados = mutableSetOf<Int>()

    private val titulosEditados = mutableMapOf<Int, String>()

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
    private var editarJob: Job? = null

    init {
        cargar()
    }

    private fun aplicarEdicion(post: Post): Post {
        return post.copy(
            title = titulosEditados[post.id] ?: post.title
        )
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
                        }.map { aplicarEdicion(it) }
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
        editarJob?.cancel()

        _eliminacion.value = null
        _edicion.value = null

        detalleJob = viewModelScope.launch {

            _detalle.value = UiState.Cargando

            repo.detalle(id)
                .onSuccess { post ->

                    repo.comentarios(id)
                        .onSuccess { comentarios ->
                            _detalle.value = UiState.Exito(
                                DetalleUi(
                                    aplicarEdicion(post),
                                    comentarios
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
        eliminarJob?.cancel()
        editarJob?.cancel()

        _eliminacion.value = null
        _edicion.value = null
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

    fun eliminar(id: Int) {

        if (_eliminacion.value == UiState.Cargando ||
            _edicion.value == UiState.Cargando ||
            id in eliminados
        ) {
            return
        }

        _eliminacion.value = UiState.Cargando

        eliminarJob = viewModelScope.launch {

            repo.eliminar(id)
                .onSuccess {

                    eliminados.add(id)
                    titulosEditados.remove(id)

                    val actual = _lista.value

                    if (actual is UiState.Exito) {
                        _lista.value = UiState.Exito(
                            actual.datos.filterNot {
                                it.id == id
                            }
                        )
                    }

                    _eliminacion.value = UiState.Exito(Unit)
                }
                .onFailure { error ->
                    _eliminacion.value = UiState.Error(
                        mensajeDe(error)
                    )
                }
        }
    }

    // MEJORA 4: Editar título de la publicación
    fun editar(id: Int, nuevoTitulo: String) {

        if (nuevoTitulo.isBlank() ||
            _edicion.value == UiState.Cargando ||
            _eliminacion.value == UiState.Cargando ||
            id in eliminados
        ) {
            return
        }

        val actualDetalle = _detalle.value

        if (actualDetalle !is UiState.Exito ||
            actualDetalle.datos.post.id != id
        ) {
            return
        }

        _edicion.value = UiState.Cargando

        editarJob = viewModelScope.launch {

            val cambios = EditarPost(
                title = nuevoTitulo.trim()
            )

            repo.editar(id, cambios)
                .onSuccess { postEditado ->

                    titulosEditados[id] = postEditado.title

                    val actual = _lista.value

                    if (actual is UiState.Exito) {

                        _lista.value = UiState.Exito(
                            actual.datos.map { post ->
                                aplicarEdicion(post)
                            }
                        )
                    }

                    val detalleActual = _detalle.value

                    if (detalleActual is UiState.Exito &&
                        detalleActual.datos.post.id == id
                    ) {

                        _detalle.value = UiState.Exito(
                            detalleActual.datos.copy(
                                post = aplicarEdicion(
                                    detalleActual.datos.post
                                )
                            )
                        )
                    }

                    _edicion.value =
                        UiState.Exito(postEditado)
                }
                .onFailure { error ->
                    _edicion.value = UiState.Error(
                        mensajeDe(error)
                    )
                }
        }
    }

    fun limpiarEdicion() {
        editarJob?.cancel()
        _edicion.value = null
    }
}

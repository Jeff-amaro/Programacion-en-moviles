package com.tecsup.productos.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tecsup.productos.data.repository.ProductoRepository
import com.tecsup.productos.domain.model.Producto
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.notification.Notificador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException

enum class TipoOrden {
    NINGUNO, PRECIO_MENOR_MAYOR, PRECIO_MAYOR_MENOR, A_Z
}

class ListaViewModel(
    private val repository: ProductoRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Producto>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Producto>>> = _estado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada.asStateFlow()

    private val _ordenActual = MutableStateFlow(TipoOrden.NINGUNO)
    val ordenActual: StateFlow<TipoOrden> = _ordenActual.asStateFlow()

    private val _categorias = MutableStateFlow<List<String>>(emptyList())
    val categorias: StateFlow<List<String>> = _categorias.asStateFlow()

    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            combine(
                repository.items,
                _textoBusqueda,
                _categoriaSeleccionada,
                _ordenActual
            ) { lista, busqueda, categoria, orden ->

                if (lista.isNotEmpty()) {
                    val categoriasExtraidas = lista.map { it.titulo.split(" ").first() }.distinct().sorted()
                    _categorias.value = categoriasExtraidas
                }

                var filtrados = if (busqueda.isNotBlank()) {
                    lista.filter { it.titulo.contains(busqueda, ignoreCase = true) || it.descripcion.contains(busqueda, ignoreCase = true) }
                } else {
                    lista
                }

                if (categoria != null) {
                    filtrados = filtrados.filter { it.titulo.split(" ").first() == categoria }
                }

                filtrados = when (orden) {
                    TipoOrden.PRECIO_MENOR_MAYOR -> filtrados.sortedBy { it.precio }
                    TipoOrden.PRECIO_MAYOR_MENOR -> filtrados.sortedByDescending { it.precio }
                    TipoOrden.A_Z -> filtrados.sortedBy { it.titulo }
                    TipoOrden.NINGUNO -> filtrados
                }

                filtrados
            }.collect { listaFiltrada ->
                if (listaFiltrada.isNotEmpty() || refrescoTerminado) {
                    _estado.value = UiState.Exito(listaFiltrada)
                }
            }
        }
        refrescar()
    }

    fun actualizarBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }

    fun seleccionarCategoria(categoria: String?) {
        _categoriaSeleccionada.value = if (_categoriaSeleccionada.value == categoria) null else categoria
    }

    fun cambiarOrden(orden: TipoOrden) {
        _ordenActual.value = orden
    }

    fun refrescar() {
        viewModelScope.launch {
            val resultado = repository.refrescar()
            refrescoTerminado = true
            resultado
                .onSuccess { n ->
                    if (_estado.value is UiState.Cargando) {
                        _estado.value = UiState.Exito(emptyList())
                    }
                    _aviso.value = null
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { e ->
                    if (repository.cantidadGuardada() == 0) {
                        _estado.value = UiState.Error(mensajeDe(e))
                    } else {
                        _aviso.value = "Sin conexión: mostrando datos guardados"
                    }
                }
        }
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            repository.alternarFavorito(id)
        }
    }

    fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
    }

    private fun mensajeDe(e: Throwable): String = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }

    companion object {
        val Factory: androidx.lifecycle.ViewModelProvider.Factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: androidx.lifecycle.viewmodel.CreationExtras
            ): T {
                val application = checkNotNull(extras[androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val api = com.tecsup.productos.data.remote.RetrofitClient.api
                val db = com.tecsup.productos.data.local.AppDatabase.obtenerInstancia(application)
                val repo = com.tecsup.productos.data.repository.ProductoRepositoryImpl(
                    api = api,
                    dao = db.productoDao(),
                    carritoDao = db.carritoDao(),
                    historialDao = db.historialDao(),
                    favoritoDao = db.favoritoDao(),
                )
                val notificador = com.tecsup.productos.notification.NotificadorAndroid(application)

                return ListaViewModel(repo, notificador) as T
            }
        }
    }
}
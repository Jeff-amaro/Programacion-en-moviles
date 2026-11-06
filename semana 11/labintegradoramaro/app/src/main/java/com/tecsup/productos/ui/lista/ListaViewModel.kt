package com.tecsup.productos.ui.lista

import androidx.lifecycle.ViewModel
import com.tecsup.productos.domain.model.Producto
import com.tecsup.productos.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListaViewModel : ViewModel() {
    private val _estado = MutableStateFlow<UiState<List<Producto>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Producto>>> = _estado.asStateFlow()

    init {
        cargarDatosDePrueba()
    }

    private fun cargarDatosDePrueba() {
        val listaPrueba = listOf(
            Producto(1, "Smartphone X", "Un teléfono genial", 999.99, "", false),
            Producto(2, "Laptop Pro", "Para trabajar rápido", 1500.0, "", true),
            Producto(3, "Perfume Elegance", "Aroma duradero", 50.0, "", false)
        )

        _estado.value = UiState.Exito(listaPrueba)
    }
}
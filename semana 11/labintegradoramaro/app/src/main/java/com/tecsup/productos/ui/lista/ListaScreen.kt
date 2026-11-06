package com.tecsup.productos.ui.lista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.productos.domain.model.Producto
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.Cargando
import com.tecsup.productos.ui.components.ErrorView

@Composable
fun ListaScreen(
    viewModel: ListaViewModel = viewModel(),
    onProductoClick: (Int) -> Unit = {}
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    when (val actual = estado) {
        is UiState.Cargando -> Cargando()
        is UiState.Error -> ErrorView(mensaje = actual.mensaje, onReintentar = { })
        is UiState.Exito -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(actual.datos) { producto ->
                    ProductoCard(producto = producto, onClick = { onProductoClick(producto.id) })
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = producto.titulo, style = MaterialTheme.typography.titleMedium)
            Text(text = "Precio: $${producto.precio}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
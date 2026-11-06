package com.tecsup.productos.ui.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.Cargando
import com.tecsup.productos.ui.components.ErrorView

@Composable
fun DetalleScreen(
    idProducto: Int,
    viewModel: DetalleViewModel = viewModel(factory = DetalleViewModel.Factory),
    onVolver: () -> Unit = {}
) {
    LaunchedEffect(idProducto) {
        viewModel.cargarDetalle(idProducto)
    }

    val estado by viewModel.estado.collectAsStateWithLifecycle()

    when (val actual = estado) {
        is UiState.Cargando -> Cargando()
        is UiState.Error -> ErrorView(
            mensaje = actual.mensaje,
            onReintentar = { viewModel.cargarDetalle(idProducto) }
        )
        is UiState.Exito -> {
            val producto = actual.datos
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = producto.titulo, style = MaterialTheme.typography.headlineMedium)
                Text(text = "Precio: $${producto.precio}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text(text = producto.descripcion, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                    Text("Volver")
                }
            }
        }
    }
}

package com.amaro.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amaro.murocampus.viewmodel.PostsViewModel
import com.amaro.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    modifier: Modifier = Modifier,
    onPostClick: (Int) -> Unit
) {
    val estado by vm.listaFiltrada.collectAsStateWithLifecycle()
    val listaCompleta by vm.lista.collectAsStateWithLifecycle()
    val usuarioFiltro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()

    // MEJORA 5: Estados de paginación
    val hayMas by vm.hayMas.collectAsStateWithLifecycle()
    val cargandoMas by vm.cargandoMas.collectAsStateWithLifecycle()
    val errorPagina by vm.errorPagina.collectAsStateWithLifecycle()

    // MEJORA 6: Estado de caché con Room
    val usandoCache by vm.usandoCache.collectAsStateWithLifecycle()

    val cantidad = when (val actual = listaCompleta) {
        is UiState.Exito -> actual.datos.size
        else -> 0
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        // Buscador de publicaciones
        OutlinedTextField(
            value = busqueda,
            onValueChange = { vm.buscar(it) },
            label = { Text("Buscar publicación") },
            placeholder = { Text("Escribe un título...") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            trailingIcon = {
                if (busqueda.isNotEmpty()) {

                    TextButton(
                        onClick = { vm.buscar("") }
                    ) {
                        Text("X")
                    }
                }
            }
        )

        // Filtros por usuario
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                FilterChip(
                    selected = usuarioFiltro == null,
                    onClick = { vm.filtrar(null) },
                    label = { Text("Todos") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1565A5),
                        selectedLabelColor = Color.White
                    )
                )
            }

            items((1..10).toList()) { userId ->

                FilterChip(
                    selected = usuarioFiltro == userId,
                    onClick = { vm.filtrar(userId) },
                    label = { Text("Usuario $userId") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1565A5),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        HorizontalDivider()

        // MEJORA 6: Aviso de almacenamiento local
        if (usandoCache && estado is UiState.Exito) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    ),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3CD)
                )
            ) {

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {

                    Text(
                        text = "Sin conexión a internet",
                        color = Color(0xFF856404),
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Mostrando publicaciones guardadas en Room",
                        color = Color(0xFF856404),
                        fontSize = 12.sp
                    )

                    TextButton(
                        onClick = { vm.cargar() }
                    ) {
                        Text(
                            text = "Actualizar",
                            color = Color(0xFF1565A5)
                        )
                    }
                }
            }
        }

        if (estado is UiState.Exito) {

            Text(
                text = "Publicaciones cargadas: $cantidad",
                color = Color(0xFF1565A5),
                fontSize = 12.sp,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            when (val actual = estado) {

                UiState.Cargando -> {
                    Cargando()
                }

                is UiState.Exito -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 8.dp,
                            bottom = 90.dp
                        )
                    ) {

                        if (actual.datos.isEmpty()) {

                            item {

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = if (busqueda.isNotBlank()) {
                                            "No se encontraron publicaciones"
                                        } else {
                                            "Sin publicaciones"
                                        }
                                    )
                                }
                            }
                        }

                        items(
                            items = actual.datos,
                            key = { it.id }
                        ) { post ->

                            TarjetaPost(
                                post = post,
                                onClick = {
                                    onPostClick(post.id)
                                }
                            )
                        }

                        // MEJORA 5: Cargar más publicaciones
                        item {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment =
                                    Alignment.CenterHorizontally,
                                verticalArrangement =
                                    Arrangement.spacedBy(10.dp)
                            ) {

                                errorPagina?.let { mensaje ->

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor =
                                                Color(0xFFFFDAD6)
                                        )
                                    ) {

                                        Text(
                                            text = mensaje,
                                            color = Color(0xFF690005),
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                }

                                if (cargandoMas) {

                                    CircularProgressIndicator(
                                        color = Color(0xFF1565A5)
                                    )

                                    Text("Cargando más publicaciones...")

                                } else if (hayMas) {

                                    Button(
                                        onClick = { vm.cargarMas() },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF1565A5),
                                            contentColor = Color.White
                                        )
                                    ) {

                                        Text(
                                            if (errorPagina != null)
                                                "Reintentar carga"
                                            else
                                                "Cargar más"
                                        )
                                    }

                                } else if (cantidad > 0) {

                                    Text(
                                        text = "No hay más publicaciones",
                                        color = MaterialTheme.colorScheme
                                            .onSurfaceVariant,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                is UiState.Error -> {

                    ErrorConReintento(
                        mensaje = actual.mensaje,
                        onReintentar = { vm.cargar() }
                    )
                }
            }
        }
    }
}

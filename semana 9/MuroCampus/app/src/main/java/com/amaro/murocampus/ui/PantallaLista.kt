
package com.amaro.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amaro.murocampus.viewmodel.PostsViewModel
import com.amaro.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    modifier: Modifier = Modifier,
    onPostClick: (Int) -> Unit
) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val usuarioFiltro by vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize()
    ) {

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

                    if (actual.datos.isEmpty()) {

                        Text(
                            text = "Sin publicaciones",
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )

                    } else {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                vertical = 8.dp
                            )
                        ) {
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

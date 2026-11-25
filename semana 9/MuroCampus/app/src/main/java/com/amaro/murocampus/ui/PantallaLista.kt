
package com.amaro.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (val actual = estado) {

            UiState.Cargando -> {
                Cargando()
            }

            is UiState.Exito -> {

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

            is UiState.Error -> {

                ErrorConReintento(
                    mensaje = actual.mensaje,
                    onReintentar = { vm.cargar() }
                )
            }
        }
    }
}

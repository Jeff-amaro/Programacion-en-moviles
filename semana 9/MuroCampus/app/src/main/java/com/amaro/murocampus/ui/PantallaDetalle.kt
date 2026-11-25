
package com.amaro.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amaro.murocampus.viewmodel.PostsViewModel
import com.amaro.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    modifier: Modifier = Modifier
) {
    val estado by vm.detalle.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (val actual = estado) {

            UiState.Cargando -> {
                Cargando("Cargando publicación...")
            }

            is UiState.Error -> {
                ErrorConReintento(
                    mensaje = actual.mensaje,
                    onReintentar = { vm.abrir(id) }
                )
            }

            is UiState.Exito -> {

                val post = actual.datos.post
                val comentarios = actual.datos.comentarios

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    item {

                        Text(
                            text = post.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = post.body,
                            fontSize = 15.sp
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        HorizontalDivider()

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "Comentarios (${comentarios.size})",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1565A5)
                        )
                    }

                    items(
                        items = comentarios,
                        key = { it.id }
                    ) { comentario ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFEDF0F5)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Text(
                                    text = comentario.email,
                                    color = Color(0xFF1565A5),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )

                                Spacer(
                                    modifier = Modifier.height(6.dp)
                                )

                                Text(
                                    text = comentario.body,
                                    color = Color(0xFF454950),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

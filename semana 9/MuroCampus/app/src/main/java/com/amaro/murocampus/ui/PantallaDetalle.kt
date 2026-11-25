
package com.amaro.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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

    val eliminacion by
    vm.eliminacion.collectAsStateWithLifecycle()

    var confirmarEliminar by rememberSaveable(id) {
        mutableStateOf(false)
    }

    if (confirmarEliminar) {

        AlertDialog(
            onDismissRequest = {
                confirmarEliminar = false
            },
            title = {
                Text("Eliminar publicación")
            },
            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar esta publicación?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        confirmarEliminar = false
                        vm.eliminar(id)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F),
                        contentColor = Color.White
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        confirmarEliminar = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        if (eliminacion is UiState.Exito) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFDDF5E3)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Publicación eliminada correctamente",
                            color = Color(0xFF216E39),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text = "Regresa a la lista de publicaciones.",
                            color = Color(0xFF216E39)
                        )
                    }
                }
            }

        } else {

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

                            // MEJORA 3: Botón eliminar
                            Button(
                                onClick = {
                                    confirmarEliminar = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled =
                                    eliminacion != UiState.Cargando,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD32F2F),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    if (eliminacion == UiState.Cargando)
                                        "Eliminando..."
                                    else
                                        "Eliminar publicación"
                                )
                            }

                            when (val resultado = eliminacion) {

                                UiState.Cargando -> {

                                    Spacer(
                                        modifier = Modifier.height(8.dp)
                                    )

                                    LinearProgressIndicator(
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                is UiState.Error -> {

                                    Spacer(
                                        modifier = Modifier.height(8.dp)
                                    )

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor =
                                                Color(0xFFFFDAD6)
                                        )
                                    ) {
                                        Text(
                                            text = resultado.mensaje,
                                            color = Color(0xFF690005),
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                }

                                else -> Unit
                            }

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
}

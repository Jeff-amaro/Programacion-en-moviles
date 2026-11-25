
package com.amaro.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.amaro.murocampus.data.PostRepositoryRemote
import com.amaro.murocampus.data.remote.RetrofitClient
import com.amaro.murocampus.ui.PantallaLista
import com.amaro.murocampus.ui.PantallaDetalle
import com.amaro.murocampus.ui.PantallaCrear
import com.amaro.murocampus.ui.theme.MuroCampusTheme
import com.amaro.murocampus.viewmodel.PostsViewModel
import com.amaro.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = PostRepositoryRemote(
            RetrofitClient.api
        )

        val factory = PostsViewModelFactory(repository)

        val vm = ViewModelProvider(
            this,
            factory
        )[PostsViewModel::class.java]

        setContent {
            MuroCampusTheme {

                var idSeleccionado by rememberSaveable {
                    mutableStateOf<Int?>(null)
                }

                var mostrarCrear by rememberSaveable {
                    mutableStateOf(false)
                }

                BackHandler(
                    enabled = idSeleccionado != null ||
                            mostrarCrear
                ) {
                    if (mostrarCrear) {

                        vm.limpiarPublicacion()
                        mostrarCrear = false

                    } else {

                        vm.cerrarDetalle()
                        idSeleccionado = null
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    topBar = {
                        TopAppBar(
                            title = {
                                Text("MuroCampus")
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF1565A5),
                                titleContentColor = Color.White
                            )
                        )
                    },

                    floatingActionButton = {

                        if (idSeleccionado == null &&
                            !mostrarCrear
                        ) {

                            FloatingActionButton(
                                onClick = {
                                    vm.limpiarPublicacion()
                                    mostrarCrear = true
                                },
                                containerColor = Color(0xFF1565A5),
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = "+",
                                    fontSize = 30.sp
                                )
                            }
                        }
                    }

                ) { innerPadding ->

                    val id = idSeleccionado

                    when {

                        mostrarCrear -> {

                            PantallaCrear(
                                vm = vm,
                                modifier = Modifier.padding(
                                    innerPadding
                                )
                            )
                        }

                        id != null -> {

                            PantallaDetalle(
                                vm = vm,
                                id = id,
                                modifier = Modifier.padding(
                                    innerPadding
                                )
                            )
                        }

                        else -> {

                            PantallaLista(
                                vm = vm,
                                modifier = Modifier.padding(
                                    innerPadding
                                ),
                                onPostClick = { postId ->
                                    vm.abrir(postId)
                                    idSeleccionado = postId
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

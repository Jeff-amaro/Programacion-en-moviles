package com.tecsup.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepositoryRoom
import com.tecsup.cafecampus.ui.navigation.NavCafe
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {

    private val sesionViewModel: SesionViewModel by viewModels {
        val database = CafeDatabase.obtener(this)
        val repository = CafeRepositoryRoom(
            productoDao = database.productoDao(),
            usuarioDao = database.usuarioDao(),
            pedidoDao = database.pedidoDao()
        )
        val preferenciasStore = PreferenciasStore(this)
        SesionViewModel.Factory(repository, preferenciasStore)
    }

    private val menuViewModel: MenuViewModel by viewModels {
        val database = CafeDatabase.obtener(this)
        val repository = CafeRepositoryRoom(
            productoDao = database.productoDao(),
            usuarioDao = database.usuarioDao(),
            pedidoDao = database.pedidoDao()
        )
        MenuViewModel.Factory(repository)
    }

    private val pedidosViewModel: PedidosClienteViewModel by viewModels {
        val database = CafeDatabase.obtener(this)
        val repository = CafeRepositoryRoom(
            productoDao = database.productoDao(),
            usuarioDao = database.usuarioDao(),
            pedidoDao = database.pedidoDao()
        )
        PedidosClienteViewModel.Factory(repository, clienteId = 2)
    }

    private val panelAdminViewModel: PanelAdminViewModel by viewModels {
        val database = CafeDatabase.obtener(this)
        val repository = CafeRepositoryRoom(
            productoDao = database.productoDao(),
            usuarioDao = database.usuarioDao(),
            pedidoDao = database.pedidoDao()
        )
        PanelAdminViewModel.Factory(repository)
    }

    private val pedidosAdminViewModel: PedidosAdminViewModel by viewModels {
        val database = CafeDatabase.obtener(this)
        val repository = CafeRepositoryRoom(
            productoDao = database.productoDao(),
            usuarioDao = database.usuarioDao(),
            pedidoDao = database.pedidoDao()
        )
        PedidosAdminViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavCafe(
                sesionViewModel = sesionViewModel,
                menuViewModel = menuViewModel,
                pedidosViewModel = pedidosViewModel,
                panelAdminViewModel = panelAdminViewModel,
                pedidosAdminViewModel = pedidosAdminViewModel
            )
        }
    }
}
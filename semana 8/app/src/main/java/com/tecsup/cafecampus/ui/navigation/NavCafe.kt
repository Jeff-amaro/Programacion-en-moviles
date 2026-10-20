package com.tecsup.cafecampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.ui.admin.DetallePedidoAdminScreen
import com.tecsup.cafecampus.ui.admin.PanelScreen
import com.tecsup.cafecampus.ui.admin.PedidosAdminScreen
import com.tecsup.cafecampus.ui.cliente.*
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

sealed class Destino(val ruta: String) {
    object Splash : Destino("splash")
    object Login : Destino("login")
    object MenuCliente : Destino("menu_cliente")
    object DetalleProducto : Destino("detalle_producto")
    object ConfirmarPedido : Destino("confirmar_pedido")
    object Seguimiento : Destino("seguimiento")
    object MisPedidos : Destino("mis_pedidos")
    object PanelAdmin : Destino("panel_admin")
    object PedidosAdmin : Destino("pedidos_admin")
    object DetallePedidoAdmin : Destino("detalle_pedido_admin")
}

@Composable
fun NavCafe(
    sesionViewModel: SesionViewModel,
    menuViewModel: MenuViewModel,
    pedidosViewModel: PedidosClienteViewModel,
    panelAdminViewModel: PanelAdminViewModel,
    pedidosAdminViewModel: PedidosAdminViewModel
) {
    val navController = rememberNavController()
    val perfilActivo by sesionViewModel.perfilActivo.collectAsState()
    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }

    NavHost(
        navController = navController,
        startDestination = Destino.Splash.ruta
    ) {
        composable(Destino.Splash.ruta) {
            LaunchedEffect(perfilActivo) {
                when (perfilActivo) {
                    "CLIENTE" -> navController.navigate(Destino.MenuCliente.ruta) {
                        popUpTo(Destino.Splash.ruta) { inclusive = true }
                    }
                    "ADMIN" -> navController.navigate(Destino.PanelAdmin.ruta) {
                        popUpTo(Destino.Splash.ruta) { inclusive = true }
                    }
                    else -> navController.navigate(Destino.Login.ruta) {
                        popUpTo(Destino.Splash.ruta) { inclusive = true }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        composable(Destino.Login.ruta) {
            LoginScreen(
                viewModel = sesionViewModel,
                onLoginExitoso = { rol ->
                    val destino = if (rol == "ADMIN") Destino.PanelAdmin.ruta else Destino.MenuCliente.ruta
                    navController.navigate(destino) {
                        popUpTo(Destino.Login.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Destino.MenuCliente.ruta) {
            MenuScreen(
                menuViewModel = menuViewModel,
                sesionViewModel = sesionViewModel,
                onProductoClick = { producto ->
                    productoSeleccionado = producto
                    navController.navigate(Destino.DetalleProducto.ruta)
                },
                onCarritoClick = {
                    navController.navigate(Destino.ConfirmarPedido.ruta)
                },
                onPedidosClick = {
                    navController.navigate(Destino.MisPedidos.ruta)
                }
            )
        }

        composable(Destino.DetalleProducto.ruta) {
            productoSeleccionado?.let { producto ->
                DetalleProductoScreen(
                    producto = producto,
                    onAgregarAlCarrito = { cantidad, nota ->
                        menuViewModel.agregar(producto, cantidad, nota)
                    },
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Destino.ConfirmarPedido.ruta) {
            ConfirmarPedidoScreen(
                menuViewModel = menuViewModel,
                pedidosViewModel = pedidosViewModel,
                onPedidoConfirmado = { id ->
                    pedidosViewModel.seleccionarPedido(id)
                    navController.navigate(Destino.Seguimiento.ruta) {
                        popUpTo(Destino.MenuCliente.ruta)
                    }
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Destino.Seguimiento.ruta) {
            SeguimientoScreen(
                pedidosViewModel = pedidosViewModel,
                onVolver = { navController.popBackStack() },
                onIrAMenu = {
                    navController.navigate(Destino.MenuCliente.ruta) {
                        popUpTo(Destino.MenuCliente.ruta) { inclusive = true }
                    }
                },
                onIrAPedidos = {
                    navController.navigate(Destino.MisPedidos.ruta)
                }
            )
        }

        composable(Destino.MisPedidos.ruta) {
            MisPedidosScreen(
                pedidosViewModel = pedidosViewModel,
                onPedidoClick = { id ->
                    pedidosViewModel.seleccionarPedido(id)
                    navController.navigate(Destino.Seguimiento.ruta)
                },
                onIrAMenu = {
                    navController.navigate(Destino.MenuCliente.ruta) {
                        popUpTo(Destino.MenuCliente.ruta) { inclusive = true }
                    }
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Destino.PanelAdmin.ruta) {
            PanelScreen(
                panelAdminViewModel = panelAdminViewModel,
                onIrAPedidos = {
                    navController.navigate(Destino.PedidosAdmin.ruta)
                }
            )
        }

        composable(Destino.PedidosAdmin.ruta) {
            PedidosAdminScreen(
                pedidosAdminViewModel = pedidosAdminViewModel,
                onPedidoClick = { id ->
                    pedidosAdminViewModel.seleccionarPedido(id)
                    navController.navigate(Destino.DetallePedidoAdmin.ruta)
                },
                onIrAPanel = {
                    navController.navigate(Destino.PanelAdmin.ruta) {
                        popUpTo(Destino.PanelAdmin.ruta) { inclusive = true }
                    }
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Destino.DetallePedidoAdmin.ruta) {
            DetallePedidoAdminScreen(
                pedidosAdminViewModel = pedidosAdminViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
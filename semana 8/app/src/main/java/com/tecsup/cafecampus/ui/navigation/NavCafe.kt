package com.tecsup.cafecampus.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.tecsup.cafecampus.ui.admin.AjustesScreen
import com.tecsup.cafecampus.ui.admin.DetallePedidoAdminScreen
import com.tecsup.cafecampus.ui.admin.FormularioProductoScreen
import com.tecsup.cafecampus.ui.admin.MenuAdminScreen
import com.tecsup.cafecampus.ui.admin.PanelScreen
import com.tecsup.cafecampus.ui.admin.PedidosAdminScreen
import com.tecsup.cafecampus.ui.cliente.*
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

sealed class Destino(val ruta: String) {
    object Splash : Destino("splash")
    object Login : Destino("login")
    object Registro : Destino("registro")
    object MenuCliente : Destino("menu_cliente")
    object DetalleProducto : Destino("detalle_producto")
    object ConfirmarPedido : Destino("confirmar_pedido")
    object Seguimiento : Destino("seguimiento")
    object MisPedidos : Destino("mis_pedidos")
    object PerfilEstudiante : Destino("perfil_estudiante")
    object PanelAdmin : Destino("panel_admin")
    object PedidosAdmin : Destino("pedidos_admin")
    object DetallePedidoAdmin : Destino("detalle_pedido_admin")
    object MenuAdmin : Destino("menu_admin")
    object FormularioProducto : Destino("formulario_producto")
    object Ajustes : Destino("ajustes")
}

@Composable
fun NavCafe(
    sesionViewModel: SesionViewModel,
    menuViewModel: MenuViewModel,
    pedidosViewModel: PedidosClienteViewModel,
    panelAdminViewModel: PanelAdminViewModel,
    pedidosAdminViewModel: PedidosAdminViewModel,
    menuAdminViewModel: MenuAdminViewModel
) {
    val navController = rememberNavController()
    val perfilActivo by sesionViewModel.perfilActivo.collectAsState()
    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }
    var productoAEditar by remember { mutableStateOf<Producto?>(null) }

    NavHost(
        navController = navController,
        startDestination = Destino.Splash.ruta,
        enterTransition = { slideInHorizontally(initialOffsetX = { 300 }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -300 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 300 }) + fadeOut() }
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
                },
                onIrARegistro = {
                    navController.navigate(Destino.Registro.ruta)
                }
            )
        }

        composable(Destino.Registro.ruta) {
            RegistroScreen(
                viewModel = sesionViewModel,
                onRegistroExitoso = {
                    navController.navigate(Destino.Login.ruta) {
                        popUpTo(Destino.Registro.ruta) { inclusive = true }
                    }
                },
                onVolverALogin = {
                    navController.popBackStack()
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
                },
                onPerfilClick = {
                    navController.navigate(Destino.PerfilEstudiante.ruta)
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
                sesionViewModel = sesionViewModel,
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
                menuViewModel = menuViewModel,
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

        composable(Destino.PerfilEstudiante.ruta) {
            PerfilEstudianteScreen(
                sesionViewModel = sesionViewModel,
                onCerrarSesion = {
                    navController.navigate(Destino.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onIrAMenu = {
                    navController.navigate(Destino.MenuCliente.ruta) {
                        popUpTo(Destino.MenuCliente.ruta) { inclusive = true }
                    }
                },
                onIrAPedidos = {
                    navController.navigate(Destino.MisPedidos.ruta)
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Destino.PanelAdmin.ruta) {
            PanelScreen(
                panelAdminViewModel = panelAdminViewModel,
                onIrAPedidos = {
                    navController.navigate(Destino.PedidosAdmin.ruta) { launchSingleTop = true }
                },
                onIrAMenu = {
                    navController.navigate(Destino.MenuAdmin.ruta) { launchSingleTop = true }
                },
                onIrAAjustes = {
                    navController.navigate(Destino.Ajustes.ruta) { launchSingleTop = true }
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
                onIrAMenu = {
                    navController.navigate(Destino.MenuAdmin.ruta) { launchSingleTop = true }
                },
                onIrAAjustes = {
                    navController.navigate(Destino.Ajustes.ruta) { launchSingleTop = true }
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

        composable(Destino.MenuAdmin.ruta) {
            MenuAdminScreen(
                menuAdminViewModel = menuAdminViewModel,
                onAgregarProducto = {
                    productoAEditar = null
                    navController.navigate(Destino.FormularioProducto.ruta)
                },
                onEditarProducto = { prod ->
                    productoAEditar = prod
                    navController.navigate(Destino.FormularioProducto.ruta)
                },
                onIrAPanel = {
                    navController.navigate(Destino.PanelAdmin.ruta) {
                        popUpTo(Destino.PanelAdmin.ruta) { inclusive = true }
                    }
                },
                onIrAPedidos = {
                    navController.navigate(Destino.PedidosAdmin.ruta) { launchSingleTop = true }
                },
                onIrAAjustes = {
                    navController.navigate(Destino.Ajustes.ruta) { launchSingleTop = true }
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Destino.FormularioProducto.ruta) {
            FormularioProductoScreen(
                menuAdminViewModel = menuAdminViewModel,
                productoExistente = productoAEditar,
                onGuardadoExitoso = { navController.popBackStack() },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Destino.Ajustes.ruta) {
            AjustesScreen(
                sesionViewModel = sesionViewModel,
                onCambiarPerfil = {
                    navController.navigate(Destino.MenuCliente.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onCerrarSesion = {
                    navController.navigate(Destino.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onIrAPanel = {
                    navController.navigate(Destino.PanelAdmin.ruta) {
                        popUpTo(Destino.PanelAdmin.ruta) { inclusive = true }
                    }
                },
                onIrAPedidos = {
                    navController.navigate(Destino.PedidosAdmin.ruta) { launchSingleTop = true }
                },
                onIrAMenu = {
                    navController.navigate(Destino.MenuAdmin.ruta) { launchSingleTop = true }
                },
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
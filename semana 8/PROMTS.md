# Registro de Prompts de IA - Café Campus

## Prompt 1 - Mejora 1: Login y Gestión de Sesión / Multi-rol
**Prompt:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un CafeRepository (interface). Agrega la pantalla de LoginScreen con gestión de sesión y soporte multi-rol (Estudiante y Administrador) persistente. La lógica de autenticación y cambio de perfil debe ir en SesionViewModel, NO en el Composable. No uses XML. Muéstrame solo los archivos que cambian.


**Qué generó la IA:** La entidad `Usuario`, `UsuarioDao`, `SesionViewModel` y la pantalla `LoginScreen.kt` con la lógica de navegación según el rol.


## Prompt 2 - Mejora 2: Seguimiento de Pedidos y Stepper
**Prompt:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un CafeRepository (interface). Agrega la pantalla SeguimientoScreen para el cliente con un indicador visual por pasos (stepper) del estado del pedido y el detalle de la orden. La lógica del estado del pedido debe ir en PedidosClienteViewModel (StateFlow), NO en el Composable. No uses XML. Muéstrame solo los archivos que cambian.


**Qué generó la IA:** El composable `SeguimientoScreen.kt`, `PedidosClienteViewModel` y la relación de datos `PedidoConDetalle`.


## Prompt 3 - Mejora 3: Panel Administrativo y CRUD de Productos
**Prompt:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un CafeRepository (interface). Agrega el panel de administración con resumen de ventas, gráfico de pedidos por hora y la gestión CRUD de productos con alternancia de disponibilidad. La lógica de negocio debe ir en PanelAdminViewModel y MenuAdminViewModel, NO en el Composable. No uses XML. Muéstrame solo los archivos que cambian.
**Qué generó la IA:** `PanelAdminViewModel`, `MenuAdminScreen.kt`, `FormularioProductoScreen.kt`, las consultas en `ProductoDao` y el gráfico de barras por hora.

## Prompt 4 - Mejora 4: Registro de Estudiante con Validación
**Prompt:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un CafeRepository (interface). Agrega la pantalla RegistroScreen para estudiantes validando que el correo no esté registrado previamente. La validación de correo único debe realizarse en SesionViewModel mediante la consulta a Room, NO en el Composable. No uses XML. Muéstrame solo los archivos que cambian.


**Qué generó la IA:** La consulta `buscarPorCorreo` en `UsuarioDao`, la función de registro en `SesionViewModel` y la interfaz de usuario `RegistroScreen.kt`.


## Prompt 5 - Mejora 5: Animaciones y Estados Vacíos
**Prompt:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un NavHost. Agrega animaciones de transición suaves entre pantallas en NavCafe.kt (slideIn, slideOut, fadeIn, fadeOut) y pantallas de estado vacío con ilustración/icono cuando no hay pedidos en MisPedidosScreen y SeguimientoScreen. La configuración debe ser 100% en Compose, NO uses XML. Muéstrame solo los archivos que cambian.


**Qué generó la IA:** Las transiciones globales en `NavCafe.kt`, la animación del indicador en `SeguimientoScreen.kt` y la vista de estado vacío en `MisPedidosScreen.kt`.


## Prompt 6 - Mejora 6: Reporte del Día
**Prompt:** Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un CafeRepository (interface). Agrega el reporte del día en PanelScreen mostrando el producto más vendido y el total por forma de pago. La lógica de agregación debe ir en consultas de PedidoDao y exponerse mediante StateFlow en PanelAdminViewModel, NO en la vista. No uses XML. Muéstrame solo los archivos que cambian.


**Qué generó la IA:** Las consultas SQL agregadas en `PedidoDao.kt`, los modelos `ProductoMasVendido` y `TotalPorFormaPago`, la exposición en `CafeRepository` y `PanelAdminViewModel`, y las tarjetas de reporte en `PanelScreen.kt`.

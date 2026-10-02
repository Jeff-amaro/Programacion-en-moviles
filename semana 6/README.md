Registro de Promts para la fase 2 de mejora con ia

Jeff Amaro


Prompt 1: Actúa como un desarrollador de aplicaciones móviles experto en Android Nativo, Kotlin, Jetpack Compose y Material Design 3. Tu objetivo es diseñar e implementar un diálogo modal interactivo (`AlertDialog`) llamado `DialogoEditarProducto` para modificar los atributos de un producto existente dentro de la lista de compras. La interfaz debe incluir campos de texto (`OutlinedTextField`) predeterminados con los valores actuales de `nombre`, `precio` y `cantidad` del modelo `Producto`, aplicando validaciones de entradas no vacías y formato numérico correcto antes de disparar el evento de confirmación para actualizar la lista en tiempo real.


---


Prompt 2:Actúa como un diseñador UI/UX y desarrollador Senior en Jetpack Compose especializado en componentes de navegación de Material Design 3. Tu objetivo es personalizar el menú lateral `ModalNavigationDrawer` (`AppDrawer.kt`) integrando en la parte superior un encabezado horizontal (`Row`) con un avatar circular (`Surface` con `CircleShape`) que muestre las iniciales "JA", acompañado por el nombre del usuario "Jeff Amaro" y su correo "jeff.amaro@tecsup.edu.pe", además de implementar un `BadgedBox` en la opción "Favoritos" que despliegue un `Badge` numérico reactivo para indicar dinámicamente la cantidad de elementos seleccionados.

---


Prompt 3:Actúa como un arquitecto de software Android experto en manejo de estados (`State Management`) y reactividad con Jetpack Compose. Tu objetivo es vincular el estado interno de la lista de productos con el menú contextual de cada tarjeta y el contador global del NavigationDrawer. Para lograrlo, extiende la data class `Producto` agregando el campo `esFavorito: Boolean = false`, configura el `DropdownMenu` en `TarjetaProducto.kt` con las opciones "Favoritos", "Compartir", "Reportar", "Editar" y "Eliminar", y realiza la elevación de estado (*State Hoisting*) hacia `AppNavegacion.kt` para que al alternar el estado de favorito de cualquier item, el contador del `Badge` en el Drawer se re-renderice instantáneamente.


---


Prompt 4:Actúa como un Android Lead Architect enfocado en buenas prácticas de programación, Clean Code y arquitectura modular en Jetpack Compose. Tu objetivo es refactorizar la arquitectura de la interfaz separando las vistas embebidas de `AppNavegacion.kt` en componentes composables totalmente independientes dentro de la carpeta `screens/`. Crea el archivo `PantallaFavoritos.kt` para mostrar únicamente los productos marcados como favoritos junto con una vista descriptiva en caso de estar vacía, crea `PantallaPerfil.kt` estructurada con una `Card` de Material 3 para desglosar la información del estudiante Jeff Amaro, e implementa un control de flujo (`when`) en `AppNavegacion.kt` para alternar dinámicamente el contenido entre Inicio, Favoritos y Perfil según la selección del menú lateral.

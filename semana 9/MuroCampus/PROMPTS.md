## Promts utilizado para laa mejoras


# Promt de la mejora 1


- Actúa como un desarrollador experto en la creación de aplicaciones móviles con Kotlin, Jetpack Compose y arquitectura MVVM. Ayúdame a implementar un interceptor de OkHttp en MuroCampus que agregue el encabezado Accept: application/json a todas las peticiones. Mantén Retrofit y el interceptor de registro de Logcat, sin modificar las demás capas.


* Que respondió :propuso crear un Interceptor, modificar la solicitud con newBuilder().header() y registrarlo en OkHttpClient


# Promt de la mejora 2 


- Actúa como un desarrollador experto en la creación de aplicaciones móviles con Kotlin, Jetpack Compose y arquitectura MVVM. Implementa un buscador que filtre las publicaciones descargadas por su título en tiempo real, sin realizar nuevas peticiones a la API. Conserva los filtros por usuario y los estados de la interfaz.


* Que respondió :propuso utilizar MutableStateFlow, combine y un OutlinedTextField para actualizar la lista según el texto ingresado.


# Promt de la mejora 3


- Actúa como un desarrollador experto en la creación de aplicaciones móviles con Kotlin, Jetpack Compose y arquitectura MVVM. Implementa la eliminación de publicaciones desde la pantalla de detalle utilizando @DELETE("posts/{id}") y Response<Unit>. Valida la respuesta en el Repository, muestra una confirmación antes de eliminar y actualiza la lista cuando la operación sea exitosa.


* Que respondió : Necesita agregar el endpoint DELETE, validar isSuccessful en el Repository y manejar los estados de la operación en el ViewModel.


# Promt de la mejora 4


- Actúa como un desarrollador experto en la creación de aplicaciones móviles con Kotlin, Jetpack Compose y arquitectura MVVM. Agrega una pantalla para editar únicamente el título de una publicación mediante PATCH. Utiliza un modelo independiente para enviar el título, muestra la respuesta y actualiza el detalle y la lista sin afectar las funciones anteriores.


* Que respondió : El modelo EditarPost, el endpoint PATCH, un estado de edición en el ViewModel y un formulario en Compose.

  
# Promt de la mejora 5


- Actúa como un desarrollador experto en la creación de aplicaciones móviles con Kotlin, Jetpack Compose y arquitectura MVVM. Implementa la carga de publicaciones de 20 en 20 utilizando @Query("_start") y @Query("_limit"), con un botón Cargar más. Mantén el buscador, los filtros, las ediciones y eliminaciones, y controla los errores de las páginas adicionales.


* Que respondió : Una consulta paginada, un desplazamiento para la siguiente página y estados para controlar la carga y los errores.


# Promt de la mejora 6


- Actúa como un desarrollador experto en la creación de aplicaciones móviles con Kotlin, Jetpack Compose y arquitectura MVVM. Integra Room en MuroCampus para almacenar las publicaciones descargadas y mostrarlas cuando no haya conexión a internet. El Repository debe decidir entre Retrofit y la base de datos local, respetar la paginación y conservar las ediciones y eliminaciones exitosas.


* Que respondió : Propuso entidades de Room, un DAO, una base de datos y un Repository que consulta primero la API y recupera la página almacenada si ocurre un error de conexión.


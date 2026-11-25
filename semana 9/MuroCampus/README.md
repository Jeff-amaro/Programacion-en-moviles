
# MuroCampus

Jeff Amaro

### Descripción del proyecto


App móvil desarrollada en kotlin y jetpack compose que consume la api de JSONPlaceholder, que nos permite visualizar, consultar, buscar y crear nuevas publicaciones y con manejos de errores de conexión 


## Estructura de paquetes


<img width="327" height="663" alt="image" src="https://github.com/user-attachments/assets/2fccd15e-8b34-4a27-a32c-867feae8341e" />


## Arquitectura MVVM


<img width="820" height="412" alt="image" src="https://github.com/user-attachments/assets/7f57856f-5001-41f0-a4ab-b01e2e6fa82c" />


## Capturas de pantalla

1. Lista de publicaciones


<img width="357" height="771" alt="image" src="https://github.com/user-attachments/assets/2ab6baa4-831d-4aec-bcf6-0ad72fd7fa5b" />


2. Detalle y comentarios


<img width="362" height="753" alt="image" src="https://github.com/user-attachments/assets/2e937f92-5d55-400d-bf31-e9e0a468f6c1" />


3. Filtro por usuario


<img width="361" height="772" alt="image" src="https://github.com/user-attachments/assets/964c4759-cad7-44d8-a643-ad161945a555" />


4. Crear publicación


<img width="357" height="785" alt="image" src="https://github.com/user-attachments/assets/4bea5284-d3c7-4b28-8a7a-7a8a7b9d6a1b" />


5. Error de conexión


<img width="360" height="762" alt="image" src="https://github.com/user-attachments/assets/d71b450c-3e97-4b38-aeba-3b1bd84897a2" />


## Para pensar


1. ¿Qué diferencia hay entre GET y POST? ¿Por qué crear lleva @Body y porUsuario lleva @Query?


- GET nos sirve para consultar información y POST para enviar nuevos datos. En este laboratorio usamos @Body para enviar el contenido de una publicación y @Query para filtrar las publicaciones de un usuario.


2. ¿Por qué la ruta es "posts" y no "/posts"?

- Porque Retrofit ya tiene una URL base y solo necesitamos agregarle la ruta posts. Si ponemos una barra al inicio, podríamos cambiar parte de la ruta original.

3. ¿Por qué el Repository devuelve Result?


- Porque así podemos controlar los errores desde el Repository y el ViewModel solo se encarga de mostrar si la consulta salió bien o mal.


4. ¿Qué pasa si olvidamos INTERNET o ignoreUnknownKeys?


- Si olvidamos el permiso de internet, la aplicación no podrá conectarse a la API. Por otro lado, si no usamos ignoreUnknownKeys, podría salir un error al recibir campos que no tenemos en nuestros modelos.


5. ¿Por qué la pantalla utiliza when sobre UiState?


- Porque con when es más fácil controlar los tres estados de la pantalla. Además, evitamos crear variables innecesarias y mantenemos el código más ordenado.


6. ¿Cómo cambiaría la aplicación con un servicio real?


- La diferencia sería que las publicaciones que creemos sí se guardarían en el servidor y podríamos verlas al actualizar la lista, cosa que actualmente no sucede porque JSONPlaceholder solo simula la creación.


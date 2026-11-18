# Prompts utilizados para las mejoras


- Mejora 1

Prompt : Actúa como un desarrollador de apps móviles experto en Kotlin. En mi app CentroTareas, mueve el progreso de una tarea de 100 pasos del ViewModel al Repository usando Flow y Dispatchers.Default. Mantén StateFlow, la cancelación con Job y las funciones existentes. Dame los archivos completos.

Que respondio : Propuso utilizar Flow<Float>, emit() y flowOn(Dispatchers.Default) para enviar el progreso.


- Mejora 2

Prompt : Actúa como un desarrollador de apps móviles experto en Kotlin. Agrega a CentroTareas un límite opcional de 2 segundos usando withTimeout. Si la carga supera ese tiempo, muestra "Tardó demasiado". Incluye un Switch para activar el límite sin afectar las cargas normales. Dame el código completo.

Que respondio : Propuso withTimeout(2000L) y manejar TimeoutCancellationException.


- Mejora 3

Promt : Actúa como un desarrollador de apps móviles experto en Kotlin. Implementa en CentroTareas hasta 3 reintentos automáticos cuando falle una carga, con esperas de 500, 1000 y 1500 ms. Respeta las cancelaciones y el límite de tiempo. Conserva las mejoras anteriores y dame el ViewModel completo.

Que respondio : Propuso reintentar con delay() de 500, 1000 y 1500 ms.


- Mejora 4

Promt : Actúa como un desarrollador de apps móviles experto en Kotlin. Agrega una barra de progreso con porcentaje al cálculo de números primos de CentroTareas, utilizando Flow, StateFlow y Dispatchers.Default. Mantén la cancelación y los resultados correctos. Dame los archivos completos sin eliminar funciones.

Que respondio : Propuso emitir porcentajes desde el Repository y mostrarlos con LinearProgressIndicator.


- Mejora 5

Promt : Actúa como un desarrollador de apps móviles experto en Kotlin. Implementa en CentroTareas un historial con DataStore que guarde los últimos 5 resultados exitosos de primos y cargas, y los recupere al reiniciar la app. Muéstralos en una tarjeta de Jetpack Compose. Conserva el código anterior.

Que respondio : Propuso utilizar Preferences DataStore y Flow para guardar y recuperar resultados.


- Mejora 6

Promt : Actúa como un desarrollador de apps móviles experto en Kotlin. Agrega a CentroTareas una tabla que compare los tiempos de las cargas secuenciales y paralelas, registre las últimas 10 mediciones y calcule el ahorro en milisegundos y porcentaje. Conserva las 5 mejoras anteriores y dame el código completo.

Que respondio : Propuso registrar los tiempos con StateFlow y calcular la diferencia en milisegundos y porcentaje.

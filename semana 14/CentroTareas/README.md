# CentroTareas


Alumno : Jeff amaro


# Descripción del proyecto


- App móvil desarrollada con kotlin y jetpack compose que nos demuestra las ejecuciones de operaciones de larga duración sin bloquear el hilo principal. Permite simular errores de conexion.


# Árbol de paquete 


<img width="411" height="532" alt="image" src="https://github.com/user-attachments/assets/c8b52da0-b5aa-49ea-8b9c-543ffd873688" />


# Arquitectura MVVM + Repository


<img width="1448" height="1086" alt="image" src="https://github.com/user-attachments/assets/76284fd0-788b-48f8-90b4-31771523b334" />


# Observación del Paso 1: congelamiento de pantalla


- Al ejecutar la tarea larga la pantalla deja de responder por 4 segundos después de ello vuelve a responder, pero cuando cambie el tiempo de 4 segundos a 10 segundos me parecio una alerta de que la app no responde y me da opciones para cerrar la app o esperar.
<img width="276" height="641" alt="Captura de pantalla 2026-11-17 145158" src="https://github.com/user-attachments/assets/0e67c13f-5a11-47b6-97cc-0d65b1561a8e" />


# Capturas de pantalla


# Captura 1: Tarea con progreso


<img width="358" height="798" alt="image" src="https://github.com/user-attachments/assets/0bec7e56-ace4-436c-97c6-072f09d5edbe" />


# Captura 2: Cancelación de tarea


<img width="361" height="775" alt="image" src="https://github.com/user-attachments/assets/733753c9-1f5b-418f-bf1f-a358d8381f83" />


# Captura 3 — Cálculo de números primos


<img width="357" height="767" alt="image" src="https://github.com/user-attachments/assets/2396eb23-2268-4da9-8bce-a602f0a4cff0" />


# Captura 4 — Comparación de cargas


- Una por una
<img width="350" height="767" alt="image" src="https://github.com/user-attachments/assets/42e70c0f-008d-41ad-bda3-1b5ed5c6430f" />


- A la vez
<img width="352" height="757" alt="image" src="https://github.com/user-attachments/assets/d07f7939-25e5-4e8f-a53b-ba6ca2b4f89f" />


# Captura 5 — Error simulado


<img width="357" height="752" alt="image" src="https://github.com/user-attachments/assets/47221e58-fb4b-4265-bc6a-fc91a8affef9" />


# Para pensar


1. ¿Por qué no se veía Trabajando... en el Paso 1?


- Por que al usar un sleep de 4 segundo este bloqueaba al hilo principal, aunque se actualizaba el valor la app no podría redibujar la interfaz hasta finalizar el bloque.


2. ¿Qué diferencia hay entre Thread.sleep y delay?


- Thread.sleep bloque el hilo donde se ejecuta sin realizar otras tareas durante la espera, en cambio , delay suspende una corrutina sin bloquear el hilo haciendo que las operaciones continuen.


3. ¿Por qué Dispatchers.Default para los primos y Dispatchers.IO para la red?


- Dispatchers.Default diseña las operaciones como los calculos y Dispatchers.IO se encarga de que esas operaciones entren y salgan del bloquesin generar bloqueos.


4. ¿Qué pasaría con la corrutina si el usuario cierra la pantalla? ¿Qué papel cumple viewModelScope?


- Si el usuario cierra la pantalla definitivamente, la corrutina se cancela. viewModelScope se encarga de controlar las corrutinas según el ciclo de vida del ViewModel, evitando que sigan ejecutándose cuando ya no se necesita
  

5. ¿Por qué async tarda tres segundos y no cinco? ¿Qué pasa si una de las dos cargas falla?


- Porque async ejecuta las dos cargas al mismo tiempo, así que solo esperamos la más lenta 3 seg, si una falla se cancela la otra y se muestra un mensaje de error.


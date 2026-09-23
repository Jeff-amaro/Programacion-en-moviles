Jeff Amaro

CAPTURAS SIN MEJORA CON IA


<img width="362" height="742" alt="Captura de pantalla 2026-09-23 102932" src="https://github.com/user-attachments/assets/420c95d6-a1e4-412b-aeca-285790ff5bcd" />


<img width="360" height="761" alt="Captura de pantalla 2026-09-23 102950" src="https://github.com/user-attachments/assets/1afbe368-cb46-429c-9d9f-655de69815ba" />


<img width="362" height="760" alt="Captura de pantalla 2026-09-23 103001" src="https://github.com/user-attachments/assets/fd254732-db81-49d2-b024-22b00c843fb6" />


<img width="356" height="732" alt="Captura de pantalla 2026-09-23 103012" src="https://github.com/user-attachments/assets/29d8c35f-4586-46bb-8c9f-110d7abab378" />


<img width="367" height="731" alt="Captura de pantalla 2026-09-23 103022" src="https://github.com/user-attachments/assets/351855a2-9315-4882-9bdf-c7b507ad1710" />


---------------------------------

CAPTURAS CON MEJORA CON IA


<img width="365" height="728" alt="image" src="https://github.com/user-attachments/assets/e6b9a4b5-6c06-40f8-bdd9-ae5f8b1460e7" />


<img width="362" height="725" alt="image" src="https://github.com/user-attachments/assets/61bbf0b1-ee74-4584-864b-baa7d0ab15a4" />


<img width="360" height="752" alt="image" src="https://github.com/user-attachments/assets/c8de204c-3e03-442b-aff4-025b1d983371" />


<img width="362" height="715" alt="image" src="https://github.com/user-attachments/assets/0071e63b-0c35-45b3-b0da-cecfa40d6c49" />


<img width="347" height="717" alt="image" src="https://github.com/user-attachments/assets/1fe30a69-0ce7-47ad-95eb-756ea2d52f06" />


<img width="365" height="708" alt="image" src="https://github.com/user-attachments/assets/165770ae-e695-46c6-a8d9-87e40854946c" />



---------------------------------

PROMT QUE SE UTILIZO PARA LA MEJORA

Actúa como un Desarrollador Senior de Android y Diseñador UX/UI experto en Jetpack Compose (Material 3). 

Necesito transformar la aplicación base "TECSUP Fit" (Fase 1) para cumplir con todos los requerimientos de la Fase 2: Rediseño visual UI/UX asistido por IA y Gestión de estado dinámica en tiempo real.

Por favor, genera la arquitectura completa y los archivos Kotlin necesarios respetando la siguiente estructura y reglas:

---

### 1. PALETA DE COLORES Y ESTILO VISUAL
- Color primario/acento: Verde corporativo (#00684A)
- Contenedores destacados: Verde claro (#E8F5E9 / #F0F4F1)
- Fondos: Blanco (#FFFFFF) y Gris súper claro (#F8FAF9)
- Tipografía: Material3 typography con variaciones de peso (Bold, Medium, SemiBold)
- Formas: Bordes redondeados (12.dp a 24.dp) y elevaciones suaves.

---

### 2. ARCHIVOS A CREAR Y MODIFICAR

#### A) `Models.kt` (Modelos de datos puros)
Define las data classes principales:
- `ClaseFit`: (id, nombre, horario, sala, cuposDisponibles, cuposTotales, esHoy, descripcion)
- `Reserva`: (id, claseNombre, horario, estado)

#### B) `MockData.kt` (Gestión de estado global reactiva)
Crea un `object MockData` que administre el estado dinámico en tiempo real usando `mutableStateListOf`:
- Datos de usuario: "Jeff Amaro", "Plan Premium", clasesCompletadas = 14, rachaDias = 3.
- Lista reactiva `clasesDisponibles` (con cupos editables).
- Lista reactiva `misReservasIniciales`.
- Función `agregarReserva(claseNombre: String, horario: String)`: agrega la nueva reserva en estado "Confirmada" arriba en la lista y resta 1 al contador de `cuposDisponibles` de la clase correspondiente.

#### C) `RutinasScreen.kt` (Nueva pantalla interactiva)
Crea una pantalla con una lista de rutinas de entrenamiento (*Tren Superior*, *Pierna & Core*, *Cardio HIIT*) con niveles. Cada tarjeta debe incluir checkboxes interactivos que al marcarlos cambien el color de fondo a verde claro indicando tarea completada.

#### D) `DetailScreen.kt` (Pantalla de detalle y acción)
Muestra la información de la clase seleccionada (banner hero, horario, sala, descripción y cupos disponibles en verde). Incluye un botón "Reservar cupo" que llame a `MockData.agregarReserva(...)` y active la navegación a la confirmación.

#### E) `ConfirmationScreen.kt` (Pantalla de éxito)
Muestra un ícono central de Check dentro de un círculo verde (`#E8F5E9`), el mensaje "¡Cupo reservado!", el nombre y horario de la clase, y un botón secundario estilizado "Ver mis reservas".

#### F) `TecsupFitApp.kt` (Navegación y ruteo central)
Configura el Scaffold con la `NavigationBar` inferior conteniendo 4 ítems:
1. Inicio (`Icons.Default.Home`)
2. Reservas (`Icons.Default.CalendarToday`)
3. Rutinas (`Icons.Default.FitnessCenter`)
4. Perfil (`Icons.Default.Person`)

Maneja los estados de navegación para alternar limpiamente entre las pestañas principales, el flujo de Detalle y la pantalla de Confirmación.

---

Entrega el código limpio, funcional y con las importaciones necesarias de Jetpack Compose.

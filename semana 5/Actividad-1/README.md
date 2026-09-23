CAPTURAS SIN LA MEJORIA CON IA EN ANDROID STUDIO

<img width="357" height="786" alt="Captura de pantalla 2026-09-22 230917" src="https://github.com/user-attachments/assets/28fdad4c-bd4c-4a58-8b45-9b9d55ad6e93" />


<img width="387" height="797" alt="Captura de pantalla 2026-09-22 230950" src="https://github.com/user-attachments/assets/0be9b0b6-3379-42fe-9280-34721a62ebec" />



<img width="361" height="785" alt="Captura de pantalla 2026-09-22 230530" src="https://github.com/user-attachments/assets/3fe4724b-5aca-47a2-99b7-74c35c922003" />


<img width="372" height="787" alt="Captura de pantalla 2026-09-22 230943" src="https://github.com/user-attachments/assets/05eadc7a-e9c7-4de1-8035-8c336d154a0d" />



------------------

CAPTURAS CON LA MEJORIA CON IA EN ANDROID STUDIO


<img width="347" height="702" alt="image" src="https://github.com/user-attachments/assets/04cedf3e-469b-4de7-8d92-81a40e7c7f03" />


<img width="342" height="720" alt="image" src="https://github.com/user-attachments/assets/20b62fbf-0014-4bf6-a260-88a04674266e" />


<img width="340" height="712" alt="image" src="https://github.com/user-attachments/assets/d641daf3-6391-4b0c-91b7-0f0a27549d08" />


<img width="342" height="713" alt="image" src="https://github.com/user-attachments/assets/3d31516a-c828-42dc-9452-01f2f5918e19" />


<img width="340" height="705" alt="image" src="https://github.com/user-attachments/assets/1f227182-2a58-4558-b123-99f5bfc712f4" />


PRONT UTILIZADO


# 📱 App NavLab - Portal Académico & Directorio de Alumnos

## 🤖 Prompt de Especificación Técnica / Rediseño con IA

> Actúa como un desarrollador de aplicaciones móviles experto en Android Nativo, Kotlin, Jetpack Compose y Material 3.
> 
> Tu objetivo es diseñar e implementar una aplicación nativa completa con temática educativa/académica. La interfaz debe tener una estética visual moderna, utilizando un degradado vertical de tonos morados y lavanda (`Brush.verticalGradient`), tarjetas translúcidas con bordes redondeados (`RoundedCornerShape(20.dp)`) e íconos intuitivos de Material Design.

---

### 🎨 1. Guía del Sistema de Diseño y UI
* **Paleta de Colores:** Fondo dinámico con degradado que transiciona de morado suave (`#6B52A1`) a lavanda claro (`#F3EDF7`).
* **Componentes:**
  * Uso de `Scaffold` con barras transparentes (`TopAppBar`) para no interrumpir el fondo en degradado.
  * Tarjetas contenedoras de opciones e inputs con bordes redondeados y sombra sutil (`CardDefaults.cardElevation`).
* **Imágenes Remotas:** Integración de la librería Coil (`AsyncImage`) para avatares e información visual de los alumnos.

---

### 🗺️ 2. Arquitectura de Navegación (`Navigation Compose`)

El sistema de rutas está definido en la clase sellada `Screen`:

1. `Screen.Login` (`"login_screen"`): Pantalla de inicio de sesión.
2. `Screen.Home` (`"home_screen"`): Dashboard principal de bienvenida.
3. `Screen.List` (`"list_screen"`): Directorio general de alumnos.
4. `Screen.Detail` (`"detail_screen/{itemId}"`): Expediente detallado del estudiante (pasa argumento entero `itemId`).
5. `Screen.Profile` (`"profile_screen"`): Perfil académico del usuario en sesión.

---

### 📱 3. Especificación de Pantallas y Flujos

#### 1️⃣ `LoginScreen.kt` (Portal Académico)
* **Diseño:** Tarjeta central flotante de login sobre fondo degradado lavanda.
* **Componentes:**
  * Campo de texto Correo Institucional con ícono de correo (`Icons.Default.Email`).
  * Campo de texto Contraseña con ocultación y botón para alternar visibilidad.
  * Botón principal "INICIAR SESIÓN" que navega a `HomeScreen`.
  * Enlace secundario para recuperar contraseña.

#### 2️⃣ `HomeScreen.kt` (Menú Principal)
* **Encabezado:** Saludo personalizado: "Bienvenido, Jeff Amaro" con subtítulo "¿Qué deseas gestionar hoy?".
* **Opciones Interactivas:**
  1. Tarjeta 1 - Directorio de Alumnos: Conduce a `ListScreen`.
  2. Tarjeta 2 - Mi Perfil Académico: Conduce a `ProfileScreen`.
* **Cierre de Sesión:** Botón inferior "Cerrar Sesión Segura" con texto e ícono en rojo (`#B3261E`), el cual limpia la pila de navegación y regresa a `LoginScreen`.

#### 3️⃣ `ListScreen.kt` (Directorio de Alumnos)
* Lista optimizada mediante `LazyColumn`.
* Muestra tarjetas de alumnos con foto (`AsyncImage`), nombre completo y carrera.
* Al presionar cualquier elemento, redirige a `DetailScreen` pasando el parámetro `itemId`.

#### 4️⃣ `ProfileScreen.kt` (Perfil Académico)
* Vista detallada con la información del usuario autenticado (Jeff Amaro), métricas de progreso e información personal.

#### 5️⃣ `DetailScreen.kt` (Expediente del Alumno)
* Recibe el parámetro `itemId` enviado desde la lista para consultar y mostrar la información académica completa del alumno seleccionado.

---

### 📦 4. Dependencias del Proyecto
```kotlin
implementation("androidx.navigation:navigation-compose:2.8.0")
implementation("androidx.compose.material3:material3:1.2.1")
implementation("io.coil-kt:coil-compose:2.6.0")




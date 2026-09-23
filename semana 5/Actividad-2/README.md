Jeff Amaro Mayhua


CAPTURAS SIN MEJORA CON IA

<img width="360" height="727" alt="Captura de pantalla 2026-09-23 082341" src="https://github.com/user-attachments/assets/9f0ef7bb-a07c-4871-bf43-6b7e9acebbc1" />


<img width="370" height="727" alt="Captura de pantalla 2026-09-23 082731" src="https://github.com/user-attachments/assets/6a45921a-81ee-4651-85c1-2d945691a926" />


<img width="360" height="747" alt="Captura de pantalla 2026-09-23 082956" src="https://github.com/user-attachments/assets/f9eab0ab-f0e7-4d2b-977c-d9c9793d77eb" />


<img width="358" height="741" alt="Captura de pantalla 2026-09-23 083014" src="https://github.com/user-attachments/assets/e7590c31-ada0-44a0-b117-d90cf919814b" />


<img width="360" height="747" alt="Captura de pantalla 2026-09-23 083041" src="https://github.com/user-attachments/assets/095ed9c0-c950-4191-bfc5-a9ede1ff90ac" />


<img width="355" height="742" alt="Captura de pantalla 2026-09-23 083055" src="https://github.com/user-attachments/assets/1a4ccb78-2856-4605-bc38-21360673ab35" />



----------------------------------------

CAPTURAS CON MEJORA CON IA


<img width="353" height="702" alt="image" src="https://github.com/user-attachments/assets/0ad7e80d-8588-434d-b329-0cfd8160f474" />


<img width="355" height="712" alt="image" src="https://github.com/user-attachments/assets/0e742d77-7e4c-4bdf-81ce-c394f2c5dfb4" />


<img width="347" height="730" alt="image" src="https://github.com/user-attachments/assets/45a17e58-3234-4c7f-8292-8018da6989ac" />


<img width="351" height="722" alt="image" src="https://github.com/user-attachments/assets/a649cdaf-e7cb-4196-9385-b6945e2f9e56" />


<img width="345" height="718" alt="image" src="https://github.com/user-attachments/assets/fb8b061e-f604-4174-a99d-affd55cbeaf2" />


<img width="345" height="706" alt="image" src="https://github.com/user-attachments/assets/232a8cac-0548-4fec-a208-e2113fb37529" />


-------------------------------------------
PROMT UTILIZADO PARA LA MEJORA


Actúa como un desarrollador Senior especializado en Android, Kotlin y Jetpack Compose (Material3). Tengo la versión base de la aplicación "Clínica Salud+" (desarrollada en la Fase 1 sin IA) y necesito refactorizarla completamente en esta Fase 2 para alinearla al 100% con las maquetas y los requerimientos de la guía del laboratorio.

Requiero implementar los siguientes componentes y corregir los detalles visuales pantalla por pantalla:

1. Arquitectura General y Navegación (ClinicaApp.kt):
   - Configurar un NavHost con Navigation Compose gestionando las rutas: "inicio", "detalle/{doctorId}", "agendar/{doctorId}", "confirmacion/{doctorName}/{fecha}/{hora}" y "mis_citas".
   - Integrar un ModalNavigationDrawer con un ancho de 280.dp y fondo blanco.
   - Encabezado del Menú Lateral: Avatar circular con el texto "JA" (color morado #4A148C en fondo #EDE7F6), nombre "Jeff Amaro" en negrita y subtítulo "Paciente" en gris, seguido de un HorizontalDivider fino (#E0E0E0).
   - Opciones del Drawer: Incluir los ítems "Inicio", "Mis citas", "Historial médico" y "Perfil".
   - Estilo del Drawer: Cada ítem debe usar un ícono personalizado de círculo delineado (DrawerCircleIcon) de 20.dp. El ítem activo debe mostrar un fondo morado claro (#F3E8F4), bordes redondeados de 16.dp, texto en negrita y color de ícono/texto en morado oscuro (#4A148C).
   - Control de TopAppBar: La TopAppBar morada (#4A148C) con el ícono de hamburguesa (≡), el título "Clínica Salud+" y el subtítulo "Hola, Jeff Amaro" SOLO debe ser visible en la pantalla "inicio" dentro del Scaffold general, evitando la duplicación de barras superiores en pantallas secundarias.

2. Pantalla Principal (HomeScreen.kt):
   - Integrar un LazyRow para los chips de filtro por especialidad (mínimo 2 opciones: "Todas", "Cardiología", "Pediatría", etc.).
   - Integrar un LazyColumn para la lista de médicos (mínimo 3). Cada tarjeta de médico debe mostrar su foto/avatar, nombre, especialidad y la calificación formateada con sus reseñas (ej. "4.9 (128 reseñas)").
   - Al presionar una tarjeta, debe navegar a "detalle/{doctorId}".

3. Detalle del Médico (DetailScreen.kt):
   - Recibir el parámetro `doctorId` mediante la ruta de navegación.
   - Mostrar la tarjeta extendida del médico con su información detallada, especialidad, calificación y años de experiencia.
   - Incluir un botón de acción principal "Agendar cita" en la parte inferior con estilo destacado en morado.

4. Selección de Cita (ScheduleScreen.kt):
   - Selección de Fecha: Mostrar una fila de tarjetas interactivas de selección única (mínimo 3 opciones: Jue 26, Vie 27, Sáb 28) con la abreviatura del día arriba y el número grande abajo.
   - Selección de Hora: Mostrar opciones de hora en chips/tarjetas de selección única (mínimo 3 opciones: 9:00 am, 10:30 am, 3:00 pm).
   - Lógica de Estado: Permitir la selección de un solo día y una sola hora a la vez, resaltando el elemento activo en morado (#4A148C) con texto blanco.
   - Botón "Confirmar cita" activo únicamente cuando se haya seleccionado fecha y hora.

5. Confirmación de Cita (ConfirmationScreen.kt):
   - Mostrar un ícono de check de confirmación en color verde dentro de un contenedor circular de fondo verde claro.
   - Tarjeta de resumen de la cita que muestre los datos recibidos por argumento: Nombre del médico, Fecha seleccionada y Hora seleccionada.
   - Botón principal de retorno "Ver mis citas" que dirija directamente al listado de citas agendadas.

6. Mis Citas (MisCitasScreen.kt):
   - Implementar un LazyColumn que despliegue la lista reactiva de citas guardadas.
   - Diferenciación visual de estados mediante badges de color: "Confirmada" (verde/morado según guía) y "Completada" (gris).
   - Lógica de cancelación mediante un AlertDialog emergente para confirmar la eliminación o cambio de estado de la cita.

Proporciona el código limpio, estructurado por archivos `.kt` (ClinicaApp, HomeScreen, DetailScreen, ScheduleScreen, ConfirmationScreen, MisCitasScreen, Models) utilizando Jetpack Compose con Material3, asegurando la cohesión visual de la paleta de colores morados y la correcta transmisión de argumentos en Navigation Compose.




# Cafe Campus

* Alumno : Jeff Amaro Mayhua

* Descripción:
  
 Una aplicacion móvil desarrollada en Jetpack Compose para la gestión de pedidos de una cafetería universitaria, implementando arquitectura MVVM, persistencia local con Room (KSP) y preferencias con DataStore.

 
 La aplicación cuenta con dos perfiles de usuario:

  
**Estudiante:** Explora el menú, gestiona su carrito, realiza pedidos y realiza seguimiento de sus estados.


**Administrador:** Gestiona el panel de control con métricas de ventas, supervisa y avanza estados de pedidos, administra el menú de productos y configura preferencias locales.


---


**Arbol de paquetes**


<img width="522" height="875" alt="image" src="https://github.com/user-attachments/assets/ef3b0719-a649-47ec-b2dc-2b61573638da" />


<img width="531" height="867" alt="image" src="https://github.com/user-attachments/assets/e35539c5-bae2-4d38-a666-80774a870cde" />


<img width="537" height="67" alt="image" src="https://github.com/user-attachments/assets/cec1a82d-0e51-438c-b5a2-74317df631f8" />


---


**Diagrama MVVM**


UI (Jetpack Compose) <---> ViewModel (StateFlow) <---> Repository (Interface) <---> Room / DataStore}


---


**Capturas del panel estudiante**


<img width="356" height="737" alt="image" src="https://github.com/user-attachments/assets/c15b4bd0-30e6-431d-b7b4-0bc642884ad5" />


<img width="356" height="732" alt="image" src="https://github.com/user-attachments/assets/958698e1-b173-49d2-9fc1-c24a933678ed" />


<img width="357" height="732" alt="image" src="https://github.com/user-attachments/assets/915971d1-c974-4ae7-8e8e-704e7f7dc747" />


---


**Capturas del panel admin**


<img width="357" height="722" alt="image" src="https://github.com/user-attachments/assets/8dafc5bf-f0b8-4832-81c8-e30c29a02c33" />


<img width="357" height="730" alt="image" src="https://github.com/user-attachments/assets/729a1266-f008-46a2-8c37-a8a365271ce4" />


<img width="358" height="726" alt="image" src="https://github.com/user-attachments/assets/ce89ffc9-a3ac-4036-89c2-78fefe0766bd" />


---


**Respuestas a la pregunta de pensar**

- ¿Por qué el Repository es una interfaz?


Para desacoplar el origen de los datos de los viewmodels, facilitando pruebas y cambios de fuente.


- ¿Por qué crearPedido es suspend y observarPedidosCliente es Flow?


por qué crearpedido  es una escritura única de acción , observaspedidoclientes emite flujos reactivos continuos.


- ¿Qué pasaría si dos perfiles usaran bases de datos distintas?


No se compartirían los datos en tiempo real entre el estudiante y el administrador.


- ¿Qué debería cambiar si mañana los datos viven en un servidor?


Reimplementar el repository con una API REST, sin alterar la capa UI.


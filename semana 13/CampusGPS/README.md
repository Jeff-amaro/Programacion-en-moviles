# CampusGPS


Alumno: Jeff Amaro


Descripción del proyecto : App móvil desarrollada con kotlin y jetpack compose que nos permite obtener la ubicación del usuario mediante el GPS y mostrarlo en Google Maps


- Árboles de paquetes del proyecto


<img width="418" height="775" alt="image" src="https://github.com/user-attachments/assets/fa6f8bf0-4ed9-4738-957e-7a3a587d7f00" />


- Arquitectura MVVM + Repository


<img width="1448" height="1086" alt="image" src="https://github.com/user-attachments/assets/adb3e4ca-bfea-4513-a877-37b0245dbb40" />


# Capturas de pantalla


- Solicitud de permiso


<img width="355" height="773" alt="image" src="https://github.com/user-attachments/assets/1c3aff73-1616-4e43-bb73-feb3831a85bd" />


- Mapa con ubicación actual


<img width="360" height="772" alt="image" src="https://github.com/user-attachments/assets/5204ae82-0ed6-4d1c-acdc-d177a5347b38" />


- Marcadores de lugares


<img width="360" height="776" alt="image" src="https://github.com/user-attachments/assets/545b0811-807b-4a34-ba7b-90b6f78a0262" />


- Lista ordenada por distancia


<img width="357" height="205" alt="image" src="https://github.com/user-attachments/assets/a8222ead-c4ab-4b46-9a8a-fcee054bedc1" />
<img width="362" height="55" alt="image" src="https://github.com/user-attachments/assets/5c814935-413e-4d3f-944b-68444960e7a2" />


- Manejo de errores


<img width="357" height="768" alt="image" src="https://github.com/user-attachments/assets/f3322dac-8e06-471f-aad4-63d4a597667c" />


<img width="360" height="773" alt="image" src="https://github.com/user-attachments/assets/98179640-3dab-4a06-b469-f2e132b34bec" />


1. ¿Por qué FusedLocationProviderClient está dentro del Repository y no en el ViewModel?


- Por qué el repository se encarga de obtener los datos del dispositivo en cambio el viewmodel es el que administra los estados y la lógica de la app


2. ¿Qué diferencia hay entre el permiso de ubicación y la clave de API de Maps? ¿Qué pasa si falta cada uno?


- El permiso autoriza a la app acceder a la posición del usuario en cambio la api nos permite utilizar google maps para mostrar la ubicación. si falta el permiso no se podrá acceder a la ubicación del usuario y si falta la api no se cargaría el mapa 


3. ¿Por qué la clave va en local.properties y no en el código? ¿Qué harías si se subiera a GitHub por error?


- Se pone ahí por que no debe publicarse en el repositorio y esto evitaria su filtración. si la clave se publica por error lo que se tendría que hacer es restringirla y reemplazar desde el google cloud y retirar la clave del repositorio


4. ¿Dónde calculas la distancia y por qué no en el Composable?


- Con la función distanciaMetros en el archivo Distancia , que se utiliza desde el viewmodel, no se calcularia en el composable por que la interfaz solo debe mostrar los datos más no realizar los calculos


5. ¿Qué pasa con tu app si el usuario niega el permiso dos veces? ¿Cómo lo manejas?


- La app mostraría un mensaje de error sin cerrarse y ofrecer un botón de ajustes para solucionarlo par que la app deja de mostrar la solicitud de permiso y el usuarios lo configure desde la app


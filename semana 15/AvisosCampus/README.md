# AvisosCampus


Jeff Amaro


# descripción


- app móvil android desarrollada con kotlin , jetpack compose y material3, que nos permite generar notificaciones locales programadas, recordatorios y detectar cambios en modo avion



# Estructura del proyecto 


<img width="365" height="692" alt="image" src="https://github.com/user-attachments/assets/6c7ef892-0f1d-4650-aeae-fdf8600d9be3" />


# Diagrama de arquitectura MVVM


<img width="1448" height="1086" alt="image" src="https://github.com/user-attachments/assets/55246f73-ef94-47f3-9809-90a1298fbd09" />


# Capturas 


-  Notificación en el panel


<img width="355" height="785" alt="image" src="https://github.com/user-attachments/assets/fe9d360e-8ce7-41e0-b66d-2a39fdb2e69b" />


-  Recordatorio con cuenta regresiva


<img width="356" height="756" alt="image" src="https://github.com/user-attachments/assets/f78c42d4-d047-46d9-828e-b5b99393ccd8" />


- Receptor del modo avión


<img width="357" height="765" alt="image" src="https://github.com/user-attachments/assets/f75f8183-51c1-4f5e-95e2-e7a3fa73c1d5" />


- Historial de avisos


<img width="355" height="765" alt="image" src="https://github.com/user-attachments/assets/ff49724d-751a-4766-8302-08404144b422" />


- Permiso denegado


<img width="356" height="757" alt="image" src="https://github.com/user-attachments/assets/e9892ef6-fcde-4c2d-bbb7-66afdc428b09" />


 # Para pensar

 
1. ¿Por qué una notificación necesita un canal y un permiso? ¿Qué pasa si falta cada uno?


- El canal organiza las notificaciones, minetras que el permiso permite qeu la app pueda enviarlas. Si faltaria alguno no se mostrara la notificacion

  
2. ¿Por qué el ViewModel emite un evento y no muestra la notificación él mismo?


- Porque el ViewModel maneja la lógica y la pantalla se encarga de mostrar las notificaciones. Así mantenemos el código ordenado.

  
3. ¿Qué pasa si usas el mismo id en dos notificaciones?


- La segunda reemplaza a la primera, por eso usamos IDs diferentes.

  
4. ¿Por qué onReceive debe ser corto y por qué hay que quitar el receptor al salir?


- Para evitar que la app se bloquee y no consumir memoria innecesariamente.

  
5. ¿En qué casos usarías una notificación local y en cuáles una push de Firebase?


- Usaría las locales para recordatorios y las de Firebase para recibir mensajes o avisos desde un servidor.

## Promt de la mejora 1


Actúa como un desarrollador de aplicaciones móviles experto en Kotlin, Jetpack Compose y arquitectura MVVM + Repository.
Ayúdame a mejorar mi aplicación CampusGPS implementando un radio de búsqueda de 100, 200 y 500 metros con Google Maps Compose. Debe dibujar un círculo alrededor del usuario, diferenciar los marcadores dentro y fuera del radio, contar los lugares cercanos y actualizar los resultados mediante StateFlow.
Conserva las funcionalidades existentes, reutiliza las distancias calculadas y respeta la arquitectura MVVM.


## Promt de la mejora 2 : 


Actúa como un desarrollador de aplicaciones móviles experto en Kotlin, Jetpack Compose, DataStore y arquitectura MVVM + Repository.
Ayúdame a implementar en CampusGPS una función que guarde el último lugar seleccionado utilizando Preferences DataStore. Al cerrar y volver a abrir la aplicación, debe recuperar el lugar y mostrarlo en una tarjeta con un botón para regresar a él en Google Maps.
Integra DataStore mediante un Repository, StateFlow y ViewModel, sin colocar Context en el ViewModel ni afectar las funcionalidades existentes.


## Promt de la mejora 3 : 


Actúa como un desarrollador de aplicaciones móviles experto en Kotlin, Jetpack Compose, Google Maps y arquitectura MVVM + Repository.
Ayúdame a implementar seguimiento GPS en tiempo real en CampusGPS utilizando `requestLocationUpdates`, `callbackFlow` y `StateFlow`. Agrega botones para iniciar y detener el seguimiento, actualiza automáticamente la ubicación, las distancias y los marcadores, y detén las actualizaciones al salir de la aplicación.
Conserva el radio inteligente, DataStore y las funcionalidades existentes, respetando la arquitectura MVVM.


## Promt de la mejora 4 :


Actúa como un desarrollador Android experto en Kotlin, Jetpack Compose, Material 3 y Google Maps Compose.
Ayúdame a implementar el modo oscuro automático en CampusGPS utilizando `isSystemInDarkTheme()`, `MapStyleOptions` y `MaterialTheme.colorScheme`. El mapa, los botones, las tarjetas y los textos deben adaptarse al tema claro u oscuro del dispositivo, manteniendo buenos contrastes y la identidad visual azul de la aplicación.
Conserva el seguimiento GPS en tiempo real, el radio inteligente, DataStore y todas las funcionalidades existentes, respetando la arquitectura MVVM.


## Promt de la mejora 5 : 


Actúa como un desarrollador Android experto en Kotlin, Jetpack Compose y Google Maps Compose.
Ayúdame a reemplazar el marcador rojo que representa al usuario en CampusGPS por el punto azul nativo de Google Maps, utilizando `MapProperties.isMyLocationEnabled`. Verifica los permisos de ubicación antes de activarlo y evita mostrar dos marcadores para la misma posición.
Conserva el seguimiento GPS en tiempo real, el círculo de búsqueda, las distancias y los marcadores de los lugares, respetando la arquitectura MVVM y sin afectar las funcionalidades existentes.


## Promt de la mejora 6 : 


Actúa como desarrollador Android experto en Kotlin, Jetpack Compose y arquitectura MVVM + Repository.
Ayúdame a implementar filtros de lugares por categoría en CampusGPS utilizando `StateFlow` y `FilterChip`. Las categorías deben obtenerse automáticamente de los lugares registrados e incluir una opción «Todas». Al seleccionar una categoría, deben actualizarse los marcadores del mapa, la lista de lugares, el conteo dentro del radio y el lugar más cercano.
Conserva todas las funcionalidades existentes y respeta la arquitectura MVVM.

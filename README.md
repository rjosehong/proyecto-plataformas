# UVG-Tours

Aplicación Android desarrollada con Kotlin y Jetpack Compose para gestionar los recorridos (tours) del campus de la UVG. La app muestra contenido distinto según el tipo de usuario que ingresa:

- Usuario: explora los tours disponibles, consulta información de las carreras de la UVG y califica los tours.
- Guía de tour (estudiante): se asigna a tours que necesitan guía, revisa su próximo tour asignado y consulta su calificación como guía.
- Administrador: revisa y aprueba las horas de servicio de los guías.

La navegación principal usa una barra inferior con tres pestañas (Tours, Feedback y Profile) cuyo contenido cambia según el rol.

## Servicios

### 1. Firebase Authentication
Servicio externo (internet). Permite el registro y el inicio de sesión de los usuarios con correo y contraseña. Cada cuenta recibe un identificador único (UID) al que se asocia el rol del usuario (usuario, guía o administrador), que define qué pantallas se muestran en la barra inferior.

### 2. Teamup Calendar API
Servicio externo (internet). Cuando un usuario se inscribe a un tour, cuando alguien programa uno nuevo o cuando un guía se asigna a uno, la app publica el tour en un calendario compartido de Teamup con el nombre, el lugar, el día, el horario y el guía asignado. El evento se crea una sola vez por tour: su identificador se guarda en el documento del tour en Firestore, y si el tour cambia, el evento se actualiza en lugar de duplicarse. La pantalla de calendario, que se abre con el botón flotante, lee esos eventos de Teamup y los muestra agrupados por día. Teamup no necesita que el usuario inicie sesión con una cuenta de Google: la app se conecta con una API key y la clave de un calendario.

### 3. WorkManager (Android Jetpack)
Componente de Android que se ejecuta en el dispositivo, sin internet. Programa recordatorios antes de cada tour (por ejemplo, una hora antes) y tareas en segundo plano que siguen funcionando aunque la app esté cerrada o el teléfono se reinicie.

### Datos locales de ejemplo (API "fake" dentro del proyecto)
Datos locales dentro del proyecto, en el paquete data/ejemplos y en las pantallas de tours. Como no hay un servidor propio para tours, horas y calificaciones, estos datos (ejemploHoras, ejemploCalificacion y sampleAsignaciones) se usan como fuente local. Las pantallas reciben listas de data classes, de modo que luego se pueden reemplazar por una base de datos o un servicio real sin cambiar el diseño.

## Librerías

Firebase Authentication:
- Firebase BoM (com.google.firebase:firebase-bom): administra las versiones de las librerías de Firebase para que sean compatibles entre sí.
- Firebase Auth (com.google.firebase:firebase-auth): realiza el registro y el inicio de sesión de los usuarios.
- Plugin google-services (com.google.gms.google-services): lee el archivo google-services.json y conecta la app con el proyecto de Firebase.

Teamup Calendar API (cliente HTTP):
- Ktor Client Core (io.ktor:ktor-client-core): cliente HTTP que hace las peticiones a la API de Teamup.
- Ktor Client Android (io.ktor:ktor-client-android): motor de red que usa Ktor en Android.
- Ktor Content Negotiation (io.ktor:ktor-client-content-negotiation): convierte automáticamente el contenido de las peticiones y respuestas.
- Ktor Serialization Kotlinx JSON (io.ktor:ktor-serialization-kotlinx-json): lee y escribe el JSON de Teamup usando Kotlinx Serialization.

WorkManager:
- AndroidX WorkManager (androidx.work:work-runtime-ktx): programa los recordatorios y las tareas en segundo plano de los tours.

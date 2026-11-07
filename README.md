# UVG-Tours

Aplicación Android desarrollada con Kotlin y Jetpack Compose para gestionar los recorridos (tours) del campus de la UVG. La app muestra contenido distinto según el tipo de usuario que ingresa:

- Usuario: explora los tours disponibles, consulta información de las carreras de la UVG y califica los tours.
- Guía de tour (estudiante): se asigna a tours que necesitan guía, revisa su próximo tour asignado y consulta su calificación como guía.
- Administrador: revisa y aprueba las horas de servicio de los guías.

La navegación principal usa una barra inferior con tres pestañas (Tours, Feedback y Profile) cuyo contenido cambia según el rol.

## Servicios

### 1. Firebase Authentication
Servicio externo (internet). Permite el registro y el inicio de sesión de los usuarios con correo y contraseña. Cada cuenta recibe un identificador único (UID) al que se asocia el rol del usuario (usuario, guía o administrador), que define qué pantallas se muestran en la barra inferior.

### 2. Google Calendar API
Servicio externo (internet). Cuando un guía se asigna a un tour, o cuando un usuario programa uno, la app crea un evento en su Google Calendar con el nombre del tour, el lugar, el día y el horario. Así el usuario ve sus tours junto a sus demás actividades. El acceso se autoriza con la cuenta de Google del usuario.

### 3. WorkManager (Android Jetpack)
Componente de Android que se ejecuta en el dispositivo, sin internet. Programa recordatorios antes de cada tour (por ejemplo, una hora antes) y tareas en segundo plano que siguen funcionando aunque la app esté cerrada o el teléfono se reinicie.

### Datos locales de ejemplo (API "fake" dentro del proyecto)
Datos locales dentro del proyecto, en el paquete data/ejemplos y en las pantallas de tours. Como no hay un servidor propio para tours, horas y calificaciones, estos datos (ejemploHoras, ejemploCalificacion y sampleAsignaciones) se usan como fuente local. Las pantallas reciben listas de data classes, de modo que luego se pueden reemplazar por una base de datos o un servicio real sin cambiar el diseño.

## Librerías

Firebase Authentication:
- Firebase BoM (com.google.firebase:firebase-bom): administra las versiones de las librerías de Firebase para que sean compatibles entre sí.
- Firebase Auth (com.google.firebase:firebase-auth): realiza el registro y el inicio de sesión de los usuarios.
- Plugin google-services (com.google.gms.google-services): lee el archivo google-services.json y conecta la app con el proyecto de Firebase.

Google Calendar API:
- Google Play Services Auth (com.google.android.gms:play-services-auth): permite elegir la cuenta de Google y obtener la autorización para usar Google Calendar.
- Google API Client for Android (com.google.api-client:google-api-client-android): maneja las credenciales y las llamadas a las APIs de Google desde Android.
- Google Calendar API v3 (com.google.apis:google-api-services-calendar): crea y consulta los eventos de los tours en Google Calendar.
- Google HTTP Client Gson (com.google.http-client:google-http-client-gson): convierte las respuestas JSON de Google Calendar en objetos de Kotlin.

WorkManager:
- AndroidX WorkManager (androidx.work:work-runtime-ktx): programa los recordatorios y las tareas en segundo plano de los tours.

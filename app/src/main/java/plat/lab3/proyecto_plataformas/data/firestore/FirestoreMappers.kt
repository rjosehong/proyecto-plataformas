package plat.lab3.proyecto_plataformas.data.firestore

import com.google.firebase.firestore.DocumentSnapshot
import plat.lab3.proyecto_plataformas.data.ejemplos.Admin
import plat.lab3.proyecto_plataformas.data.ejemplos.CuadroHoras
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import plat.lab3.proyecto_plataformas.data.ejemplos.calificacionGuia
object Collections {
    const val TOURS = "tours"
    const val HORAS = "horas"
    const val CALIFICACIONES = "calificaciones"
    const val ADMIN = "admin"
}

private fun DocumentSnapshot.int(key: String, default: Int = 0): Int =
    (get(key) as? Number)?.toInt() ?: default

private fun DocumentSnapshot.double(key: String): Double =
    (get(key) as? Number)?.toDouble() ?: 0.0

private fun DocumentSnapshot.text(key: String): String = getString(key).orEmpty()

fun DocumentSnapshot.toTour() = Tour(
    tourId = int("tourId", id.toIntOrNull() ?: 0),
    horas = double("horas"),
    tour_nombre = text("tour_nombre"),
    personas_registradas = int("personas_registradas"),
    dia = text("dia"),
    persona_asignada = text("persona_asignada"),
    guia_necesitado = int("guia_necesitado"),
    lugar = text("lugar"),
    hora_inicial = int("hora_inicial"),
    hora_final = int("hora_final")
)

fun DocumentSnapshot.toCuadroHoras() = CuadroHoras(
    horas = double("horas"),
    tour_nombre = text("tour_nombre"),
    Checked_In = int("checked_in"),
    tour_Slots = int("tour_slots"),
    dia = text("dia"),
    persona_asignada = text("persona_asignada")
)

fun DocumentSnapshot.toCalificacionGuia() = calificacionGuia(
    calificacion = double("calificacion"),
    nombre = text("nombre")
)

fun DocumentSnapshot.toAdmin() = Admin(
    tours_semanales = int("tours_semanales"),
    tours_noAsignados = int("tours_no_asignados"),
    tours_completos_por_asignar = int("tours_completos_por_asignar"),
    guiasDeTour = int("guias_de_tour")
)

fun Tour.toMap(): Map<String, Any> = mapOf(
    "tourId" to tourId,
    "horas" to horas,
    "tour_nombre" to tour_nombre,
    "personas_registradas" to personas_registradas,
    "dia" to dia,
    "persona_asignada" to persona_asignada,
    "guia_necesitado" to guia_necesitado,
    "lugar" to lugar,
    "hora_inicial" to hora_inicial,
    "hora_final" to hora_final
)

fun CuadroHoras.toMap(): Map<String, Any> = mapOf(
    "horas" to horas,
    "tour_nombre" to tour_nombre,
    "checked_in" to Checked_In,
    "tour_slots" to tour_Slots,
    "dia" to dia,
    "persona_asignada" to persona_asignada
)

fun calificacionGuia.toMap(): Map<String, Any> = mapOf(
    "calificacion" to calificacion,
    "nombre" to nombre
)

fun Admin.toMap(): Map<String, Any> = mapOf(
    "tours_semanales" to tours_semanales,
    "tours_no_asignados" to tours_noAsignados,
    "tours_completos_por_asignar" to tours_completos_por_asignar,
    "guias_de_tour" to guiasDeTour
)

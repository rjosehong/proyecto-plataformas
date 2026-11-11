package plat.lab3.proyecto_plataformas.data.calendar

import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
private fun tourDate(tour: Tour): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH)
    return try {
        val monthAndDay = tour.dia.substringAfter(",").trim()
        LocalDate.parse("$monthAndDay ${LocalDate.now().year}", formatter)
    } catch (e: Exception) {
        LocalDate.now().plusDays(1)
    }
}

private fun millisAt(date: LocalDate, hour: Int): Long =
    date.atTime(hour.coerceIn(0, 23), 0)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

fun tourStartMillis(tour: Tour): Long = millisAt(tourDate(tour), tour.hora_inicial)

fun tourEndMillis(tour: Tour): Long = millisAt(tourDate(tour), tour.hora_final)

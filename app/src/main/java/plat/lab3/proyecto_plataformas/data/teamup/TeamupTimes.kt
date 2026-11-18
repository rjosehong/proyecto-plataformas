package plat.lab3.proyecto_plataformas.data.teamup

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val offsetWithoutColon = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxx")

fun parseTeamupTime(value: String): ZonedDateTime? {
    val zone = ZoneId.systemDefault()
    return try {
        OffsetDateTime.parse(value).atZoneSameInstant(zone)
    } catch (e: Exception) {
        try {
            OffsetDateTime.parse(value, offsetWithoutColon).atZoneSameInstant(zone)
        } catch (e2: Exception) {
            null
        }
    }
}

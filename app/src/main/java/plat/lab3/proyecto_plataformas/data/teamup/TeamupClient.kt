package plat.lab3.proyecto_plataformas.data.teamup

import android.net.http.HttpResponseCache.install
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import java.io.IOException
import java.time.LocalDate

class TeamupException(val status: Int, message: String) : Exception(message)

class TeamupNotConfiguredException :
    Exception("Falta configurar Teamup: completa TeamupConfig.kt")

private val teamupJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
    encodeDefaults = true
}

// Todas las funciones son suspend: llámalas desde una corrutina (viewModelScope.launch).
object TeamupClient {

    private val http = HttpClient(Android) {
        engine {
            connectTimeout = 10_000
            socketTimeout = 15_000
        }
        install(ContentNegotiation) { json(teamupJson) }
        defaultRequest {
            // Teamup pide la API key en este encabezado en cada petición
            header("Teamup-Token", TeamupConfig.API_KEY)
        }
    }

    private fun url(path: String) = "${TeamupConfig.BASE_URL}/${TeamupConfig.CALENDAR_KEY}/$path"

    private var cachedSubcalendarId: Int? = null

    suspend fun getEvents(from: LocalDate, to: LocalDate): List<TeamupEvent> {
        ensureConfigured()
        val response = http.get(url("events")) {
            parameter("startDate", from.toString())
            parameter("endDate", to.toString())
        }
        response.requireSuccess()
        return response.body<TeamupEventsResponse>().events
    }

    suspend fun createEvent(request: TeamupEventRequest): TeamupEvent {
        ensureConfigured()
        val response = http.post(url("events")) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        response.requireSuccess()
        return response.body<TeamupEventResponse>().event
            ?: throw TeamupException(response.status.value, "Teamup no devolvió el evento creado")
    }

    suspend fun updateEvent(eventId: String, request: TeamupEventRequest) {
        ensureConfigured()
        val current = http.get(url("events/$eventId"))
        current.requireSuccess()
        val currentEvent = current.body<JsonObject>()["event"]?.jsonObject
            ?: throw TeamupException(current.status.value, "Teamup no devolvió el evento")

        val merged = buildJsonObject {
            currentEvent.forEach { (key, value) -> put(key, value) }
            put("title", JsonPrimitive(request.title))
            put("start_dt", JsonPrimitive(request.startDt))
            put("end_dt", JsonPrimitive(request.endDt))
            put("location", JsonPrimitive(request.location))
            put("who", JsonPrimitive(request.who))
            put("notes", JsonPrimitive(request.notes))
        }
        val response = http.put(url("events/$eventId")) {
            contentType(ContentType.Application.Json)
            setBody(merged)
        }
        response.requireSuccess()
    }

    suspend fun resolveSubcalendarId(): Int {
        if (TeamupConfig.SUBCALENDAR_ID != 0) return TeamupConfig.SUBCALENDAR_ID
        cachedSubcalendarId?.let { return it }

        ensureConfigured()
        val response = http.get(url("subcalendars"))
        response.requireSuccess()
        val subcalendars = response.body<TeamupSubcalendarsResponse>().subcalendars
        val chosen = subcalendars.firstOrNull { it.active } ?: subcalendars.firstOrNull()
            ?: throw TeamupException(
                response.status.value,
                "El calendario de Teamup no tiene subcalendarios. Crea uno en Settings."
            )
        cachedSubcalendarId = chosen.id
        return chosen.id
    }

    private fun ensureConfigured() {
        if (!TeamupConfig.isConfigured()) throw TeamupNotConfiguredException()
    }

    private suspend fun HttpResponse.requireSuccess() {
        if (!status.isSuccess()) {
            throw TeamupException(status.value, bodyAsText().take(300))
        }
    }
}

fun teamupErrorMessage(e: Throwable): String = when {
    e is TeamupNotConfiguredException -> e.message.orEmpty()
    e is TeamupException && (e.status == 401 || e.status == 403) ->
        "Teamup rechazó la API key o el calendario no permite modificar eventos."
    e is TeamupException && e.status == 404 ->
        "Teamup no encontró el calendario. Revisa la clave en TeamupConfig.kt."
    e is TeamupException -> "Teamup respondió con un error (${e.status})."
    e is IOException -> "Sin conexión a internet. Intenta de nuevo."
    else -> e.localizedMessage ?: "No se pudo conectar con Teamup."
}

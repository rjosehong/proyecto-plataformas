package plat.lab3.proyecto_plataformas.data.teamup

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import plat.lab3.proyecto_plataformas.data.calendar.tourEndMillis
import plat.lab3.proyecto_plataformas.data.calendar.tourStartMillis
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import plat.lab3.proyecto_plataformas.data.firestore.Collections
import java.time.Instant
import java.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object TeamupRepository {

    private const val EVENT_ID_FIELD = "teamup_event_id"

    suspend fun syncTour(tour: Tour): Result<Unit> = safe {
        val request = tour.toTeamupRequest(TeamupClient.resolveSubcalendarId())
        val tourDoc = FirebaseFirestore.getInstance()
            .collection(Collections.TOURS)
            .document(tour.tourId.toString())

        val existingId = tourDoc.get().awaitTask().getString(EVENT_ID_FIELD)
        if (existingId.isNullOrBlank()) {
            val created = TeamupClient.createEvent(request)
            created.id?.let { id ->
                tourDoc.set(mapOf(EVENT_ID_FIELD to id), SetOptions.merge()).awaitTask()
            }
        } else {
            TeamupClient.updateEvent(existingId, request)
        }
    }

    suspend fun events(from: LocalDate, to: LocalDate): Result<List<TeamupEvent>> = safe {
        TeamupClient.getEvents(from, to)
    }

    private fun Tour.toTeamupRequest(subcalendarId: Int) = TeamupEventRequest(
        subcalendarIds = listOf(subcalendarId),
        title = tour_nombre,
        startDt = Instant.ofEpochMilli(tourStartMillis(this)).toString(),
        endDt = Instant.ofEpochMilli(tourEndMillis(this)).toString(),
        location = lugar,
        who = if (guia_necesitado > 0) "Guía por asignar" else persona_asignada,
        notes = "Horas de servicio: $horas. Personas registradas: $personas_registradas."
    )
}

private suspend fun <T> safe(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    Result.failure(e)
}

private suspend fun <T> Task<T>.awaitTask(): T = suspendCancellableCoroutine { cont ->
    addOnSuccessListener { cont.resume(it) }
    addOnFailureListener { cont.resumeWithException(it) }
}

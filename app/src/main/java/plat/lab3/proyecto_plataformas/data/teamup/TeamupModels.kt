package plat.lab3.proyecto_plataformas.data.teamup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamupEvent(
    val id: String? = null,
    val title: String = "",
    val who: String? = null,
    val location: String? = null,
    val notes: String? = null,
    @SerialName("start_dt") val startDt: String = "",
    @SerialName("end_dt") val endDt: String = "",
    @SerialName("all_day") val allDay: Boolean = false
)

@Serializable
data class TeamupEventsResponse(val events: List<TeamupEvent> = emptyList())

@Serializable
data class TeamupEventResponse(val event: TeamupEvent? = null)

@Serializable
data class TeamupSubcalendar(
    val id: Int,
    val name: String = "",
    val active: Boolean = true
)

@Serializable
data class TeamupSubcalendarsResponse(val subcalendars: List<TeamupSubcalendar> = emptyList())

@Serializable
data class TeamupEventRequest(
    @SerialName("subcalendar_ids") val subcalendarIds: List<Int>,
    val title: String,
    @SerialName("start_dt") val startDt: String,
    @SerialName("end_dt") val endDt: String,
    val location: String = "",
    val who: String = "",
    val notes: String = "",
    @SerialName("all_day") val allDay: Boolean = false
)

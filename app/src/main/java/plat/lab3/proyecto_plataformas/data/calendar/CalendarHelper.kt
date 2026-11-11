package plat.lab3.proyecto_plataformas.data.calendar

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import java.util.TimeZone

object CalendarHelper {
    fun insertTourEvent(accessToken: String, tour: Tour): String? {
        val service = Calendar.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance()
        ) { request ->
            request.headers.setAuthorization("Bearer $accessToken")
        }.setApplicationName("UVG-Tours").build()

        val timeZone = TimeZone.getDefault().id
        val event = Event()
            .setSummary("Tour ${tour.tour_nombre}")
            .setLocation(tour.lugar)
            .setStart(
                EventDateTime()
                    .setDateTime(DateTime(tourStartMillis(tour)))
                    .setTimeZone(timeZone)
            )
            .setEnd(
                EventDateTime()
                    .setDateTime(DateTime(tourEndMillis(tour)))
                    .setTimeZone(timeZone)
            )

        return service.events().insert("primary", event).execute().htmlLink
    }
}

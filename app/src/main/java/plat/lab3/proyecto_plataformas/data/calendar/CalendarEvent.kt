package plat.lab3.proyecto_plataformas.ui.screens.calendar

sealed interface CalendarEvent {
    object Refresh : CalendarEvent
}

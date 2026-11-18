package plat.lab3.proyecto_plataformas.ui.screens.calendar

import java.time.LocalDate

data class CalendarItem(
    val title: String,
    val time: String,
    val location: String,
    val who: String
)

data class CalendarDay(
    val date: LocalDate,
    val label: String,
    val events: List<CalendarItem>
)

data class CalendarState(
    val isLoading: Boolean = true,
    val days: List<CalendarDay> = emptyList(),
    val errorMessage: String? = null
)

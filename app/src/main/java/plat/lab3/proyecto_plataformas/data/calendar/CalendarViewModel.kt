package plat.lab3.proyecto_plataformas.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.data.teamup.TeamupEvent
import plat.lab3.proyecto_plataformas.data.teamup.TeamupRepository
import plat.lab3.proyecto_plataformas.data.teamup.parseTeamupTime
import plat.lab3.proyecto_plataformas.data.teamup.teamupErrorMessage
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarState())
    val uiState: StateFlow<CalendarState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.Refresh -> loadEvents()
        }
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val today = LocalDate.now()
            TeamupRepository.events(today.minusDays(60), today.plusDays(120))
                .onSuccess { events ->
                    _uiState.update { it.copy(isLoading = false, days = events.toDays()) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = teamupErrorMessage(e))
                    }
                }
        }
    }
}

private fun List<TeamupEvent>.toDays(): List<CalendarDay> {
    val dayFormat = DateTimeFormatter.ofPattern("EEE, MMM dd", Locale.ENGLISH)
    val timeFormat = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)

    return mapNotNull { event ->
        val start = parseTeamupTime(event.startDt) ?: return@mapNotNull null
        val end = parseTeamupTime(event.endDt)
        val time = when {
            event.allDay -> "Todo el día"
            end != null -> "${start.format(timeFormat)} - ${end.format(timeFormat)}"
            else -> start.format(timeFormat)
        }
        start to CalendarItem(
            title = event.title,
            time = time,
            location = event.location.orEmpty(),
            who = event.who.orEmpty()
        )
    }
        .sortedBy { it.first.toInstant() }
        .groupBy { it.first.toLocalDate() }
        .map { (date, items) ->
            CalendarDay(
                date = date,
                label = date.format(dayFormat),
                events = items.map { it.second }
            )
        }
}

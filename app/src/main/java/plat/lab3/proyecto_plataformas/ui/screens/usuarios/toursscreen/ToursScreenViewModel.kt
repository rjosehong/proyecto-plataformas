package plat.lab3.proyecto_plataformas.ui.screens.usuarios.toursscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import plat.lab3.proyecto_plataformas.data.firestore.ToursRepository
import plat.lab3.proyecto_plataformas.data.firestore.firestoreErrorMessage

class ToursScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ToursScreenState())
    val uiState: StateFlow<ToursScreenState> = _uiState.asStateFlow()

    init {
        observeTours()
    }

    private fun observeTours() {
        viewModelScope.launch {
            ToursRepository.tours()
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = firestoreErrorMessage(e))
                    }
                }
                .collect { toursList ->
                    _uiState.update {
                        it.copy(isLoading = false, tours = toursList, errorMessage = null)
                    }
                }
        }
    }

    fun onEvent(event: ToursScreenEvent) {
        when (event) {
            is ToursScreenEvent.SelectTab -> {
                _uiState.update { it.copy(selectedTab = event.tabIndex) }
            }
            is ToursScreenEvent.OpenDialog -> {
                _uiState.update { it.copy(activeDialog = event.dialog) }
            }
            is ToursScreenEvent.DismissDialog -> {
                _uiState.update { it.copy(activeDialog = UserTourDialog.NONE) }
            }
            is ToursScreenEvent.RegisterToTour -> {
                ToursRepository.registerUserToTour(event.tour.tourId) { result ->
                    result.onSuccess {
                        _uiState.update {
                            it.copy(
                                registeredTourIds = it.registeredTourIds + event.tour.tourId,
                                pendingCalendarTour = event.tour,
                                feedbackMessage = "Inscrito en ${event.tour.tour_nombre}"
                            )
                        }
                    }.onFailure { e ->
                        _uiState.update {
                            it.copy(feedbackMessage = firestoreErrorMessage(e))
                        }
                    }
                }
            }
            is ToursScreenEvent.CreateNewTour -> {
                val nextId = (_uiState.value.tours.maxOfOrNull { it.tourId } ?: 0) + 1
                val duracion = (event.horaFinal - event.horaInicial).coerceAtLeast(1).toDouble()
                val newTour = Tour(
                    tourId = nextId,
                    horas = duracion,
                    tour_nombre = event.nombre.trim(),
                    personas_registradas = 1,
                    dia = event.dia.trim(),
                    persona_asignada = "Por asignar",
                    guia_necesitado = 1,
                    lugar = event.lugar.trim(),
                    hora_inicial = event.horaInicial,
                    hora_final = event.horaFinal
                )

                ToursRepository.createTour(newTour) { result ->
                    result.onSuccess {
                        _uiState.update {
                            it.copy(
                                activeDialog = UserTourDialog.NONE,
                                registeredTourIds = it.registeredTourIds + newTour.tourId,
                                pendingCalendarTour = newTour,
                                feedbackMessage = "Tour programado y guardado en Firestore"
                            )
                        }
                    }.onFailure { e ->
                        _uiState.update {
                            it.copy(feedbackMessage = firestoreErrorMessage(e))
                        }
                    }
                }
            }
            is ToursScreenEvent.ClearPendingCalendarTour -> {
                _uiState.update { it.copy(pendingCalendarTour = null) }
            }
            is ToursScreenEvent.ShowFeedback -> {
                _uiState.update { it.copy(feedbackMessage = event.message) }
            }
            is ToursScreenEvent.ClearFeedback -> {
                _uiState.update { it.copy(feedbackMessage = null) }
            }
        }
    }
}
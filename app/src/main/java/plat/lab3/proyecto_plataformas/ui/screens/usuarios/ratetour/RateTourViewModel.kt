package plat.lab3.proyecto_plataformas.ui.screens.usuarios.ratetour

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.data.ejemplos.calificacionGuia
import plat.lab3.proyecto_plataformas.data.firestore.ToursRepository
import plat.lab3.proyecto_plataformas.data.firestore.firestoreErrorMessage

class RateTourViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RateTourState())
    val uiState: StateFlow<RateTourState> = _uiState.asStateFlow()

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
                    _uiState.update { current ->
                        val stillSelected = current.selectedTour?.let { sel ->
                            toursList.firstOrNull { it.tourId == sel.tourId }
                        } ?: toursList.firstOrNull()

                        current.copy(
                            isLoading = false,
                            availableTours = toursList,
                            selectedTour = stillSelected,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun onEvent(event: RateTourEvent) {
        when (event) {
            is RateTourEvent.SelectTour -> {
                _uiState.update { it.copy(selectedTour = event.tour) }
            }
            is RateTourEvent.ScoreChanged -> {
                val filtered = event.value.filter { it.isDigit() || it == '.' }
                _uiState.update { it.copy(scoreInput = filtered) }
            }
            is RateTourEvent.CommentChanged -> {
                _uiState.update { it.copy(commentInput = event.value) }
            }
            is RateTourEvent.SubmitRating -> {
                val state = _uiState.value
                val tour = state.selectedTour
                if (tour == null) {
                    _uiState.update { it.copy(feedbackMessage = "Selecciona un tour primero") }
                    return
                }

                val nota = state.scoreInput.toDoubleOrNull()
                if (nota == null || nota < 0.0 || nota > 100.0) {
                    _uiState.update { it.copy(feedbackMessage = "Ingresa una nota válida entre 0 y 100") }
                    return
                }

                val guia = tour.persona_asignada.ifBlank { "Sin guía asignado" }
                val detalle = buildString {
                    append("$guia — ${tour.tour_nombre}")
                    if (state.commentInput.isNotBlank()) {
                        append(" (${state.commentInput.trim()})")
                    }
                }

                val nuevaCalificacion = calificacionGuia(
                    calificacion = nota,
                    nombre = detalle
                )

                _uiState.update { it.copy(isSubmitting = true) }

                ToursRepository.saveCalificacion(nuevaCalificacion) { result ->
                    result.onSuccess {
                        _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                scoreInput = "",
                                commentInput = "",
                                feedbackMessage = "Calificación de $nota/100 enviada a $guia"
                            )
                        }
                    }.onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                feedbackMessage = firestoreErrorMessage(e)
                            )
                        }
                    }
                }
            }
            is RateTourEvent.ClearFeedback -> {
                _uiState.update { it.copy(feedbackMessage = null) }
            }
        }
    }
}
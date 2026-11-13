package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.asignaciontour

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.data.auth.AuthRepository
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import plat.lab3.proyecto_plataformas.data.firestore.ToursRepository
import plat.lab3.proyecto_plataformas.data.firestore.UsersRepository
import plat.lab3.proyecto_plataformas.data.firestore.firestoreErrorMessage

class AsignacionTourViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AsignacionTourState())
    val uiState: StateFlow<AsignacionTourState> = _uiState.asStateFlow()

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

    fun onEvent(event: AsignacionTourEvent) {
        when (event) {
            is AsignacionTourEvent.SelectTab -> {
                _uiState.update { it.copy(selectedTab = event.tabIndex) }
            }
            is AsignacionTourEvent.SetFilterAvailable -> {
                _uiState.update { it.copy(filterOnlyAvailable = event.onlyAvailable) }
            }
            is AsignacionTourEvent.ClaimSlot -> {
                val targetTour = _uiState.value.tours.firstOrNull { it.tourId == event.tourId } ?: return
                viewModelScope.launch {
                    val uid = AuthRepository.currentUid()
                    val profileName = uid?.let { UsersRepository.profile(it).firstOrNull()?.nombre }
                    val guideName = profileName?.takeIf { it.isNotBlank() }
                        ?: AuthRepository.currentEmail()?.substringBefore("@")
                        ?: "Guía UVG"

                    ToursRepository.claimGuideSlot(event.tourId, guideName) { result ->
                        result.onSuccess {
                            _uiState.update {
                                it.copy(
                                    claimedTourForSync = targetTour.copy(
                                        guia_necesitado = 0,
                                        persona_asignada = guideName
                                    ),
                                    feedbackMessage = "Tour asignado a $guideName"
                                )
                            }
                        }.onFailure { e ->
                            _uiState.update {
                                it.copy(feedbackMessage = firestoreErrorMessage(e))
                            }
                        }
                    }
                }
            }
            is AsignacionTourEvent.ClearClaimedTourForSync -> {
                _uiState.update { it.copy(claimedTourForSync = null) }
            }
            is AsignacionTourEvent.ShowFeedback -> {
                _uiState.update { it.copy(feedbackMessage = event.message) }
            }
            is AsignacionTourEvent.ClearFeedback -> {
                _uiState.update { it.copy(feedbackMessage = null) }
            }
        }
    }
}
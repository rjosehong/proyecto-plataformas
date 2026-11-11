package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.asignaciontour

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.data.firestore.ToursRepository
import plat.lab3.proyecto_plataformas.data.firestore.firestoreErrorMessage

class AsignacionTourViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AsignacionTourState())
    val uiState: StateFlow<AsignacionTourState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadTours()
    }

    fun onEvent(event: AsignacionTourEvent) {
        when (event) {
            is AsignacionTourEvent.SelectTab -> {
                _uiState.update { it.copy(selectedTab = event.tabIndex) }
            }
            is AsignacionTourEvent.ClaimSlot -> {
            }
            is AsignacionTourEvent.RefreshTours -> {
                loadTours()
            }
        }
    }

    private fun loadTours() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        loadJob = viewModelScope.launch {
            ToursRepository.tours()
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = firestoreErrorMessage(e))
                    }
                }
                .collect { tours ->
                    _uiState.update {
                        it.copy(isLoading = false, tours = tours, errorMessage = null)
                    }
                }
        }
    }
}

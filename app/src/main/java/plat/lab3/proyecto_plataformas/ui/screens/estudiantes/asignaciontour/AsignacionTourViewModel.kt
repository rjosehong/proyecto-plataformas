package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.asignaciontour

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemplosDeTours

class AsignacionTourViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AsignacionTourState())
    val uiState: StateFlow<AsignacionTourState> = _uiState.asStateFlow()

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
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        try {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    tours = ejemplosDeTours,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Error al cargar los tours"
                )
            }
        }
    }
}
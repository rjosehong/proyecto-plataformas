package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.scores

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemploCalificacion

class ScoresViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScoresState())
    val uiState: StateFlow<ScoresState> = _uiState.asStateFlow()

    init {
        loadScores()
    }

    fun onEvent(event: ScoresEvent) {
        when (event) {
            is ScoresEvent.RefreshScores -> {
                loadScores()
            }
        }
    }

    private fun loadScores() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        try {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    calificaciones = ejemploCalificacion,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Error al cargar las calificaciones"
                )
            }
        }
    }
}
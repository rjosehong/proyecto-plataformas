package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.scores

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

class ScoresViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScoresState())
    val uiState: StateFlow<ScoresState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

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
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        loadJob = viewModelScope.launch {
            ToursRepository.calificaciones()
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = firestoreErrorMessage(e))
                    }
                }
                .collect { calificaciones ->
                    _uiState.update {
                        it.copy(isLoading = false, calificaciones = calificaciones, errorMessage = null)
                    }
                }
        }
    }
}

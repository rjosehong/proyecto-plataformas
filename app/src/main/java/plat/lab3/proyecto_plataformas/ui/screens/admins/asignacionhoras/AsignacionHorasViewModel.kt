package plat.lab3.proyecto_plataformas.ui.screens.admins.asignacionhoras

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

class AsignacionHorasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AsignacionHorasState())
    val uiState: StateFlow<AsignacionHorasState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadHoras()
    }

    fun loadHoras() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        loadJob = viewModelScope.launch {
            ToursRepository.horas()
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = firestoreErrorMessage(e))
                    }
                }
                .collect { horas ->
                    _uiState.update {
                        it.copy(isLoading = false, data = horas, errorMessage = null)
                    }
                }
        }
    }
}

package plat.lab3.proyecto_plataformas.ui.screens.admins.gestionadmin

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

class GestionAdminViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GestionAdminState())
    val uiState: StateFlow<GestionAdminState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadAdminData()
    }

    fun onEvent(event: GestionAdminEvent) {
        when (event) {
            is GestionAdminEvent.RefreshData -> {
                loadAdminData()
            }
        }
    }

    private fun loadAdminData() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        loadJob = viewModelScope.launch {
            ToursRepository.adminResumen()
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = firestoreErrorMessage(e))
                    }
                }
                .collect { resumen ->
                    _uiState.update {
                        it.copy(isLoading = false, adminData = resumen, errorMessage = null)
                    }
                }
        }
    }
}

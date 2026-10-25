package plat.lab3.proyecto_plataformas.ui.screens.admins.asignacionhoras

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemploHoras

class AsignacionHorasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AsignacionHorasState())
    val uiState: StateFlow<AsignacionHorasState> = _uiState.asStateFlow()

    init {
        loadHoras()
    }

    fun loadHoras() {
        // 1. Estado de carga (Loading)
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        try {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    data = ejemploHoras,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Ocurrió un error inesperado"
                )
            }
        }
    }
}
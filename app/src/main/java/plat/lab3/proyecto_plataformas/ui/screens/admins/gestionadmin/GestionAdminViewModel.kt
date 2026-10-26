package plat.lab3.proyecto_plataformas.ui.screens.admins.gestionadmin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemploAdminView

class GestionAdminViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GestionAdminState())
    val uiState: StateFlow<GestionAdminState> = _uiState.asStateFlow()

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
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        try {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    adminData = ejemploAdminView,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Error al cargar los datos."
                )
            }
        }
    }
}
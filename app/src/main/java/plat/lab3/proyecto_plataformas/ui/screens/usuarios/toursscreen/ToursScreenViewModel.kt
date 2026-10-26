package plat.lab3.proyecto_plataformas.ui.screens.usuarios.toursscreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ToursScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ToursScreenState())
    val uiState: StateFlow<ToursScreenState> = _uiState.asStateFlow()

    fun onEvent(event: ToursScreenEvent) {
        when (event) {
            is ToursScreenEvent.SelectTab -> {
                _uiState.update { it.copy(selectedTab = event.tabIndex) }
            }
            is ToursScreenEvent.RefreshData -> {
            }
        }
    }
}
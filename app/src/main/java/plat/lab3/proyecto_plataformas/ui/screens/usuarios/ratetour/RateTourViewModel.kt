package plat.lab3.proyecto_plataformas.ui.screens.usuarios.ratetour

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RateTourViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RateTourState())
    val uiState: StateFlow<RateTourState> = _uiState.asStateFlow()

    fun onEvent(event: RateTourEvent) {
        when (event) {
            is RateTourEvent.TourSelected -> {
                _uiState.update { it.copy(selectedTour = event.tourName, isDropdownOpen = false) }
            }
            is RateTourEvent.CommentsChanged -> {
                _uiState.update { it.copy(comments = event.comments) }
            }
            is RateTourEvent.ToggleDropdown -> {
                _uiState.update { it.copy(isDropdownOpen = event.isOpen) }
            }
            is RateTourEvent.SubmitRating -> {
                _uiState.update { it.copy(isLoading = true) }
                _uiState.update { it.copy(isLoading = false, comments = "", selectedTour = "") }
            }
        }
    }
}
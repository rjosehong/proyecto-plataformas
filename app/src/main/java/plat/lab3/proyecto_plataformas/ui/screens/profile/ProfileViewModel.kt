package plat.lab3.proyecto_plataformas.ui.screens.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import plat.lab3.proyecto_plataformas.data.auth.AuthRepository

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState(email = AuthRepository.currentEmail().orEmpty()))
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Logout -> {
                AuthRepository.logout()
                event.onLogoutClick()
            }
        }
    }
}
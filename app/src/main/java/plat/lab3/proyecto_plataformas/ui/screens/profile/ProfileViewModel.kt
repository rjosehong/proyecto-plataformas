package plat.lab3.proyecto_plataformas.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.data.auth.AuthRepository
import plat.lab3.proyecto_plataformas.data.firestore.UsersRepository

class ProfileViewModel : ViewModel() {

    private val email = AuthRepository.currentEmail().orEmpty()

    private val _uiState = MutableStateFlow(
        ProfileState(nombre = email.substringBefore("@"), carne = "N/A", email = email)
    )
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val uid = AuthRepository.currentUid() ?: return
        viewModelScope.launch {
            UsersRepository.profile(uid)
                .catch {}
                .collect { profile ->
                    _uiState.update {
                        it.copy(
                            nombre = profile?.nombre?.takeIf { n -> n.isNotBlank() }
                                ?: email.substringBefore("@"),
                            carne = profile?.carne?.takeIf { c -> c.isNotBlank() } ?: "N/A"
                        )
                    }
                }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Logout -> {
                AuthRepository.logout()
                event.onLogoutClick()
            }
        }
    }
}

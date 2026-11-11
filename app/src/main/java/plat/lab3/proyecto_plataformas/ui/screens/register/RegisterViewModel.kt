package plat.lab3.proyecto_plataformas.ui.screens.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import plat.lab3.proyecto_plataformas.data.auth.AuthRepository
import plat.lab3.proyecto_plataformas.data.auth.authErrorMessage

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
            }
            is RegisterEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
            }
            is RegisterEvent.SubmitRegister -> {
                val current = _uiState.value
                if (current.email.isBlank() || current.password.isBlank()) {
                    _uiState.update { it.copy(errorMessage = "Completa todos los campos") }
                    return
                }
                if (current.password.length < 6) {
                    _uiState.update {
                        it.copy(errorMessage = "La contraseña debe tener al menos 6 caracteres")
                    }
                    return
                }
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                AuthRepository.register(current.email, current.password) { result ->
                    result
                        .onSuccess { role ->
                            _uiState.update { it.copy(isLoading = false) }
                            event.onRegisterDone(role)
                        }
                        .onFailure { error ->
                            _uiState.update {
                                it.copy(isLoading = false, errorMessage = authErrorMessage(error))
                            }
                        }
                }
            }
        }
    }
}

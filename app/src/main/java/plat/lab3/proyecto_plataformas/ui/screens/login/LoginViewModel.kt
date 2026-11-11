package plat.lab3.proyecto_plataformas.ui.screens.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import plat.lab3.proyecto_plataformas.data.auth.AuthRepository
import plat.lab3.proyecto_plataformas.data.auth.authErrorMessage

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
            }
            is LoginEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
            }
            is LoginEvent.SubmitLogin -> {
                val current = _uiState.value
                if (current.email.isBlank() || current.password.isBlank()) {
                    _uiState.update { it.copy(errorMessage = "Ingresa tu correo y tu contraseña") }
                    return
                }
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                AuthRepository.login(current.email, current.password) { result ->
                    result
                        .onSuccess { role ->
                            _uiState.update { it.copy(isLoading = false) }
                            event.onEnterClick(role)
                        }
                        .onFailure { error ->
                            _uiState.update {
                                it.copy(isLoading = false, errorMessage = authErrorMessage(error))
                            }
                        }
                }
            }
            is LoginEvent.OnRegisterClicked -> {
            }
        }
    }
}

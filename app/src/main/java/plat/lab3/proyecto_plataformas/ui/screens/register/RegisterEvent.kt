package plat.lab3.proyecto_plataformas.ui.screens.register

sealed interface RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent
    data class PasswordChanged(val password: String) : RegisterEvent
    data class SubmitRegister(val onRegisterDone: () -> Unit) : RegisterEvent
}
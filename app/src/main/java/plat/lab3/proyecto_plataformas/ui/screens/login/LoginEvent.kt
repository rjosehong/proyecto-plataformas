package plat.lab3.proyecto_plataformas.ui.screens.login

import plat.lab3.proyecto_plataformas.navigation.Role

sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data class SubmitLogin(val role: Role, val onEnterClick: (Role) -> Unit) : LoginEvent
    object OnRegisterClicked : LoginEvent
}
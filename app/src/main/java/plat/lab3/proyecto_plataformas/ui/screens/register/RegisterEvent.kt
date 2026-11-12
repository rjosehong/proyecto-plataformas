package plat.lab3.proyecto_plataformas.ui.screens.register

import plat.lab3.proyecto_plataformas.navigation.Role

sealed interface RegisterEvent {
    data class NombreChanged(val nombre: String) : RegisterEvent
    data class EmailChanged(val email: String) : RegisterEvent
    data class PasswordChanged(val password: String) : RegisterEvent
    data class CarneChanged(val carne: String) : RegisterEvent
    data class SubmitRegister(val onRegisterDone: (Role) -> Unit) : RegisterEvent
}

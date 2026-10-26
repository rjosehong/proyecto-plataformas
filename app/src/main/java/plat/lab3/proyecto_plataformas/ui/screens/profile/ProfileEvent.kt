package plat.lab3.proyecto_plataformas.ui.screens.profile

sealed interface ProfileEvent {
    data class Logout(val onLogoutClick: () -> Unit) : ProfileEvent
}
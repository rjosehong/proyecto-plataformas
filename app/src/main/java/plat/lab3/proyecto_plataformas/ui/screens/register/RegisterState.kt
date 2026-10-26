package plat.lab3.proyecto_plataformas.ui.screens.register

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
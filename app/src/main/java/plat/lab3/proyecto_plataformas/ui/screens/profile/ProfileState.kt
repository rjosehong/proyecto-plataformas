package plat.lab3.proyecto_plataformas.ui.screens.profile

data class ProfileState(
    val nombre: String = "",
    val carne: String = "N/A",
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

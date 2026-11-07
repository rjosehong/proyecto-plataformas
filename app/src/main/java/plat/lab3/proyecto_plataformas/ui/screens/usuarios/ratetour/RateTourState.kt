package plat.lab3.proyecto_plataformas.ui.screens.usuarios.ratetour

data class RateTourState(
    val selectedTour: String = "",
    val comments: String = "",
    val isDropdownOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
package plat.lab3.proyecto_plataformas.ui.screens.usuarios.toursscreen

data class ToursScreenState(
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
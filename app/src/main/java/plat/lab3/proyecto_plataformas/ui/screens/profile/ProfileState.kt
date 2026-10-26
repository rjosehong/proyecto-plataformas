package plat.lab3.proyecto_plataformas.ui.screens.profile

data class ProfileState(
    val nombre: String = "Rodrigo José Navas Hong",
    val carne: String = "25589",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
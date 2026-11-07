package plat.lab3.proyecto_plataformas.ui.screens.admins.gestionadmin

import plat.lab3.proyecto_plataformas.data.ejemplos.Admin

data class GestionAdminState(
    val adminData: List<Admin> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
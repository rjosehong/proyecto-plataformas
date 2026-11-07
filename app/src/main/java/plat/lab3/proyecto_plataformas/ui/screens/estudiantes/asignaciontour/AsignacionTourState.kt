package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.asignaciontour

import plat.lab3.proyecto_plataformas.data.ejemplos.Tour

data class AsignacionTourState(
    val tours: List<Tour> = emptyList(),
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
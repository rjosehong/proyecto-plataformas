package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.asignaciontour

import plat.lab3.proyecto_plataformas.data.ejemplos.Tour

data class AsignacionTourState(
    val selectedTab: Int = 0,
    val filterOnlyAvailable: Boolean = false,
    val tours: List<Tour> = emptyList(),
    val claimedTourForSync: Tour? = null,
    val feedbackMessage: String? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
package plat.lab3.proyecto_plataformas.ui.screens.usuarios.ratetour

import plat.lab3.proyecto_plataformas.data.ejemplos.Tour

data class RateTourState(
    val availableTours: List<Tour> = emptyList(),
    val selectedTour: Tour? = null,
    val scoreInput: String = "",
    val commentInput: String = "",
    val isSubmitting: Boolean = false,
    val feedbackMessage: String? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
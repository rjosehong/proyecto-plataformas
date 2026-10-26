package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.scores

import plat.lab3.proyecto_plataformas.data.ejemplos.calificacionGuia

data class ScoresState(
    val calificaciones: List<calificacionGuia> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
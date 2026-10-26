package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.scores

sealed interface ScoresEvent {
    object RefreshScores : ScoresEvent
}
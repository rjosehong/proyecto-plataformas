package plat.lab3.proyecto_plataformas.ui.screens.usuarios.ratetour

import plat.lab3.proyecto_plataformas.data.ejemplos.Tour

sealed interface RateTourEvent {
    data class SelectTour(val tour: Tour) : RateTourEvent
    data class ScoreChanged(val value: String) : RateTourEvent
    data class CommentChanged(val value: String) : RateTourEvent
    object SubmitRating : RateTourEvent
    object ClearFeedback : RateTourEvent
}
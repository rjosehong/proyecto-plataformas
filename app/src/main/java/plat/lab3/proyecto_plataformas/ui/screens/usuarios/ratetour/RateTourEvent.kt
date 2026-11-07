package plat.lab3.proyecto_plataformas.ui.screens.usuarios.ratetour

sealed interface RateTourEvent {
    data class TourSelected(val tourName: String) : RateTourEvent
    data class CommentsChanged(val comments: String) : RateTourEvent
    data class ToggleDropdown(val isOpen: Boolean) : RateTourEvent
    object SubmitRating : RateTourEvent
}
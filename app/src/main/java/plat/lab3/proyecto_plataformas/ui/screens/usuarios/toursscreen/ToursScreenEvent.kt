package plat.lab3.proyecto_plataformas.ui.screens.usuarios.toursscreen

import plat.lab3.proyecto_plataformas.data.ejemplos.Tour

sealed interface ToursScreenEvent {
    data class SelectTab(val tabIndex: Int) : ToursScreenEvent
    data class OpenDialog(val dialog: UserTourDialog) : ToursScreenEvent
    object DismissDialog : ToursScreenEvent
    data class RegisterToTour(val tour: Tour) : ToursScreenEvent
    data class CreateNewTour(
        val nombre: String,
        val lugar: String,
        val dia: String,
        val horaInicial: Int,
        val horaFinal: Int
    ) : ToursScreenEvent
    object ClearPendingCalendarTour : ToursScreenEvent
    data class ShowFeedback(val message: String) : ToursScreenEvent
    object ClearFeedback : ToursScreenEvent
}
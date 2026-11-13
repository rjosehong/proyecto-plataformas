package plat.lab3.proyecto_plataformas.ui.screens.usuarios.toursscreen

import plat.lab3.proyecto_plataformas.data.ejemplos.Tour

enum class UserTourDialog {
    NONE,
    CONSULTAR_TOURS,
    PROGRAMAR_TOUR,
    INFO_CARRERAS
}

data class ToursScreenState(
    val selectedTab: Int = 0,
    val tours: List<Tour> = emptyList(),
    val registeredTourIds: Set<Int> = emptySet(),
    val activeDialog: UserTourDialog = UserTourDialog.NONE,
    val pendingCalendarTour: Tour? = null,
    val feedbackMessage: String? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
package plat.lab3.proyecto_plataformas.ui.screens.admins.asignacionhoras

sealed interface AsignacionHorasEvent {
    data class AprobarHoras(val tourId: Int) : AsignacionHorasEvent
    data class ReviewTour(val tourId: Int) : AsignacionHorasEvent
    object RefreshData : AsignacionHorasEvent
}
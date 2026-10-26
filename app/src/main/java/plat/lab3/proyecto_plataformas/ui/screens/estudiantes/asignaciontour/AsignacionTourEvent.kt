package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.asignaciontour

sealed interface AsignacionTourEvent {
    data class SelectTab(val tabIndex: Int) : AsignacionTourEvent
    data class ClaimSlot(val tourId: Int) : AsignacionTourEvent
    object RefreshTours : AsignacionTourEvent
}
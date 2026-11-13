package plat.lab3.proyecto_plataformas.ui.screens.admins.asignacionhoras

sealed interface AsignacionHorasEvent {
    data class SelectTab(val tabIndex: Int) : AsignacionHorasEvent
}
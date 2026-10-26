package plat.lab3.proyecto_plataformas.ui.screens.usuarios.toursscreen

sealed interface ToursScreenEvent {
    data class SelectTab(val tabIndex: Int) : ToursScreenEvent
    object RefreshData : ToursScreenEvent
}
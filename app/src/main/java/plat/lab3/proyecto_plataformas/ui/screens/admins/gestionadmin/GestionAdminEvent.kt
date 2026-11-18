package plat.lab3.proyecto_plataformas.ui.screens.admins.gestionadmin

sealed interface GestionAdminEvent {
    object RefreshData : GestionAdminEvent
    object OpenCreateTour : GestionAdminEvent
    object DismissCreateTour : GestionAdminEvent
    data class CreateTour(
        val nombre: String,
        val lugar: String,
        val dia: String,
        val horaInicial: Int,
        val horaFinal: Int,
        val guiasNecesarios: Int
    ) : GestionAdminEvent
    object ClearFeedback : GestionAdminEvent
}

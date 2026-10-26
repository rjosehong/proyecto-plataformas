package plat.lab3.proyecto_plataformas.ui.screens.admins.gestionadmin

sealed interface GestionAdminEvent {
    object RefreshData : GestionAdminEvent
}
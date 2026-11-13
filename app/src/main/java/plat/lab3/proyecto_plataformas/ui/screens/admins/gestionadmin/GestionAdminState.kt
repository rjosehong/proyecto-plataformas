package plat.lab3.proyecto_plataformas.ui.screens.admins.gestionadmin

data class GuiaCargaItem(
    val nombre: String,
    val toursAsignados: Int,
    val horasTotales: Double
)

data class GestionAdminState(
    val toursSemanales: Int = 0,
    val toursSinAsignar: Int = 0,
    val toursAsignados: Int = 0,
    val guiasPendientesDeAsignar: Int = 0,
    val guiasTotales: Int = 0,
    val horasTotalesProgramadas: Double = 0.0,
    val cargaPorGuia: List<GuiaCargaItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
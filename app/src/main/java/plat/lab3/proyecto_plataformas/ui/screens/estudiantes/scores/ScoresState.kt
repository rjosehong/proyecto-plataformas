package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.scores

data class GuiaRatingItem(
    val tourNombre: String,
    val comentario: String,
    val calificacion: Double
)

data class ScoresState(
    val guideName: String = "",
    val guideEmail: String = "",
    val promedioTotal: Double = 0.0,
    val totalEvaluaciones: Int = 0,
    val calificacionesRecientes: List<GuiaRatingItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
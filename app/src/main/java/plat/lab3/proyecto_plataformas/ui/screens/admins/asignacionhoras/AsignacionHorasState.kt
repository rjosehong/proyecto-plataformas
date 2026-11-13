package plat.lab3.proyecto_plataformas.ui.screens.admins.asignacionhoras

import plat.lab3.proyecto_plataformas.data.ejemplos.CuadroHoras
import plat.lab3.proyecto_plataformas.data.ejemplos.calificacionGuia

data class GuiaPromedioItem(
    val guiaNombre: String,
    val promedio: Double,
    val totalEvaluaciones: Int
)

data class AsignacionHorasState(
    val selectedTab: Int = 0,
    val data: List<CuadroHoras> = emptyList(),
    val calificaciones: List<calificacionGuia> = emptyList(),
    val promediosPorGuia: List<GuiaPromedioItem> = emptyList(),
    val promedioGeneral: Double = 0.0,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
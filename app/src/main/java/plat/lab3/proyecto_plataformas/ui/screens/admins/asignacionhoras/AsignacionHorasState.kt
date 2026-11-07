package plat.lab3.proyecto_plataformas.ui.screens.admins.asignacionhoras

import plat.lab3.proyecto_plataformas.data.ejemplos.CuadroHoras

data class AsignacionHorasState(
    val isLoading: Boolean = false,
    val data: List<CuadroHoras> = emptyList(),
    val errorMessage: String? = null
)
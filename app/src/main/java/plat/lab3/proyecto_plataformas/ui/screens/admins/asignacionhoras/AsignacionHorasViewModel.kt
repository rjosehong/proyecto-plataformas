package plat.lab3.proyecto_plataformas.ui.screens.admins.asignacionhoras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.data.firestore.ToursRepository
import plat.lab3.proyecto_plataformas.data.firestore.firestoreErrorMessage

class AsignacionHorasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AsignacionHorasState())
    val uiState: StateFlow<AsignacionHorasState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun onEvent(event: AsignacionHorasEvent) {
        when (event) {
            is AsignacionHorasEvent.SelectTab -> {
                _uiState.update { it.copy(selectedTab = event.tabIndex) }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            launch {
                ToursRepository.horas()
                    .catch { e -> _uiState.update { it.copy(errorMessage = firestoreErrorMessage(e)) } }
                    .collect { list -> _uiState.update { it.copy(data = list) } }
            }

            launch {
                ToursRepository.calificaciones()
                    .catch { e -> _uiState.update { it.copy(errorMessage = firestoreErrorMessage(e)) } }
                    .collect { list ->
                        val promedioGlobal = if (list.isNotEmpty()) {
                            list.map { it.calificacion }.average()
                        } else {
                            0.0
                        }

                        val porGuia = list
                            .groupBy { calif ->
                                calif.nombre
                                    .substringBefore(" — ")
                                    .substringBefore(" (")
                                    .trim()
                                    .ifBlank { "Guía sin nombre" }
                            }
                            .map { (guia, notas) ->
                                GuiaPromedioItem(
                                    guiaNombre = guia,
                                    promedio = notas.map { it.calificacion }.average(),
                                    totalEvaluaciones = notas.size
                                )
                            }
                            .sortedByDescending { it.promedio }

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                calificaciones = list.reversed(),
                                promediosPorGuia = porGuia,
                                promedioGeneral = promedioGlobal
                            )
                        }
                    }
            }
        }
    }
}
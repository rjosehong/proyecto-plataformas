package plat.lab3.proyecto_plataformas.ui.screens.admins.gestionadmin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.data.firestore.ToursRepository
import plat.lab3.proyecto_plataformas.data.firestore.firestoreErrorMessage

class GestionAdminViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GestionAdminState())
    val uiState: StateFlow<GestionAdminState> = _uiState.asStateFlow()

    init {
        observeRealTimeMetrics()
    }

    private fun observeRegisteredGuidesCount(): Flow<Set<String>> = callbackFlow {
        val registration = FirebaseFirestore.getInstance()
            .collection("usuarios")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptySet())
                    return@addSnapshotListener
                }
                val names = snapshot?.documents
                    ?.mapNotNull { doc ->
                        val nombre = doc.getString("nombre")?.trim().orEmpty()
                        val email = doc.getString("email")?.trim().orEmpty()
                        when {
                            nombre.isNotBlank() -> nombre
                            email.isNotBlank() -> email.substringBefore("@")
                            else -> null
                        }
                    }
                    ?.toSet()
                    ?: emptySet()
                trySend(names)
            }
        awaitClose { registration.remove() }
    }

    private fun observeRealTimeMetrics() {
        viewModelScope.launch {
            combine(
                ToursRepository.tours(),
                observeRegisteredGuidesCount()
            ) { tours, registeredUsers ->
                val totalSemanales = tours.size

                val sinAsignarList = tours.filter { tour ->
                    tour.guia_necesitado > 0 ||
                            tour.persona_asignada.isBlank() ||
                            tour.persona_asignada.equals("Por asignar", ignoreCase = true)
                }
                val asignadosList = tours.filterNot { it in sinAsignarList }

                val totalSinAsignar = sinAsignarList.size
                val totalAsignados = asignadosList.size

                val pendientesDeAsignar = tours.sumOf { tour ->
                    if (tour.guia_necesitado > 0) {
                        tour.guia_necesitado
                    } else if (tour.persona_asignada.isBlank() ||
                        tour.persona_asignada.equals("Por asignar", ignoreCase = true)
                    ) {
                        1
                    } else {
                        0
                    }
                }

                val guiasEnTours = asignadosList
                    .map { it.persona_asignada.trim() }
                    .filter { it.isNotBlank() && !it.equals("Por asignar", ignoreCase = true) }
                    .toSet()

                val totalGuiasUnicos = (guiasEnTours + registeredUsers).size

                val cargaPorGuia = asignadosList
                    .groupBy { it.persona_asignada.trim() }
                    .map { (guia, listaTours) ->
                        GuiaCargaItem(
                            nombre = guia,
                            toursAsignados = listaTours.size,
                            horasTotales = listaTours.sumOf { it.horas }
                        )
                    }
                    .sortedByDescending { it.toursAsignados }

                GestionAdminState(
                    toursSemanales = totalSemanales,
                    toursSinAsignar = totalSinAsignar,
                    toursAsignados = totalAsignados,
                    guiasPendientesDeAsignar = pendientesDeAsignar,
                    guiasTotales = totalGuiasUnicos,
                    horasTotalesProgramadas = tours.sumOf { it.horas },
                    cargaPorGuia = cargaPorGuia,
                    isLoading = false,
                    errorMessage = null
                )
            }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = firestoreErrorMessage(e)
                        )
                    }
                }
                .collect { newState ->
                    _uiState.value = newState
                }
        }
    }
}
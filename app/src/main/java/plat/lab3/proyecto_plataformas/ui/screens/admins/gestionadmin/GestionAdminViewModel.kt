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
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import plat.lab3.proyecto_plataformas.data.firestore.ToursRepository
import plat.lab3.proyecto_plataformas.data.firestore.firestoreErrorMessage
import plat.lab3.proyecto_plataformas.data.teamup.TeamupRepository
import plat.lab3.proyecto_plataformas.data.teamup.teamupErrorMessage

class GestionAdminViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GestionAdminState())
    val uiState: StateFlow<GestionAdminState> = _uiState.asStateFlow()

    private val _createState = MutableStateFlow(AdminCreateTourState())
    val createState: StateFlow<AdminCreateTourState> = _createState.asStateFlow()

    private var latestTours: List<Tour>? = null

    init {
        observeRealTimeMetrics()
    }

    fun onEvent(event: GestionAdminEvent) {
        when (event) {
            is GestionAdminEvent.RefreshData -> Unit
            is GestionAdminEvent.OpenCreateTour -> {
                _createState.update { it.copy(showDialog = true, dialogError = null) }
            }
            is GestionAdminEvent.DismissCreateTour -> {
                _createState.update { it.copy(showDialog = false, dialogError = null) }
            }
            is GestionAdminEvent.ClearFeedback -> {
                _createState.update { it.copy(feedbackMessage = null) }
            }
            is GestionAdminEvent.CreateTour -> createTour(event)
        }
    }

    private fun createTour(event: GestionAdminEvent.CreateTour) {
        val tours = latestTours
        if (tours == null) {
            _createState.update {
                it.copy(dialogError = "Espera a que carguen los tours e intenta de nuevo")
            }
            return
        }

        val newTour = Tour(
            tourId = (tours.maxOfOrNull { it.tourId } ?: 0) + 1,
            horas = (event.horaFinal - event.horaInicial).coerceAtLeast(1).toDouble(),
            tour_nombre = event.nombre.trim(),
            personas_registradas = 0,
            dia = event.dia,
            persona_asignada = "Por asignar",
            guia_necesitado = event.guiasNecesarios,
            lugar = event.lugar.trim(),
            hora_inicial = event.horaInicial,
            hora_final = event.horaFinal
        )

        _createState.update { it.copy(isSaving = true, dialogError = null) }
        ToursRepository.createTour(newTour) { result ->
            result.onSuccess {
                _createState.update {
                    it.copy(
                        isSaving = false,
                        showDialog = false,
                        feedbackMessage = "Tour creado: ${newTour.tour_nombre}"
                    )
                }
                syncWithTeamup(newTour)
            }.onFailure { e ->
                _createState.update {
                    it.copy(isSaving = false, dialogError = firestoreErrorMessage(e))
                }
            }
        }
    }

    private fun syncWithTeamup(tour: Tour) {
        viewModelScope.launch {
            TeamupRepository.syncTour(tour)
                .onSuccess {
                    _createState.update {
                        it.copy(feedbackMessage = "Tour creado y agregado al calendario de Teamup")
                    }
                }
                .onFailure { e ->
                    _createState.update {
                        it.copy(
                            feedbackMessage = "Tour creado, pero no se pudo actualizar Teamup: " +
                                teamupErrorMessage(e)
                        )
                    }
                }
        }
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
                latestTours = tours
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
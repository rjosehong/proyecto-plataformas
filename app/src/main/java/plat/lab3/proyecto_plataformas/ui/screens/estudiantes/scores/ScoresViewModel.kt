package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.scores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.data.auth.AuthRepository
import plat.lab3.proyecto_plataformas.data.ejemplos.calificacionGuia
import plat.lab3.proyecto_plataformas.data.firestore.ToursRepository
import plat.lab3.proyecto_plataformas.data.firestore.UsersRepository
import plat.lab3.proyecto_plataformas.data.firestore.firestoreErrorMessage

class ScoresViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScoresState())
    val uiState: StateFlow<ScoresState> = _uiState.asStateFlow()

    init {
        observeCurrentGuidePerformance()
    }

    fun onEvent(event: ScoresEvent) {
        when (event) {
            is ScoresEvent.Refresh -> observeCurrentGuidePerformance()
        }
    }

    private fun observeCurrentGuidePerformance() {
        val uid = AuthRepository.currentUid()
        val email = AuthRepository.currentEmail().orEmpty()
        val emailAlias = email.substringBefore("@").trim()

        val profileFlow = if (uid != null) {
            UsersRepository.profile(uid).catch { emit(null) }
        } else {
            flowOf(null)
        }

        viewModelScope.launch {
            combine(
                profileFlow,
                ToursRepository.calificaciones()
            ) { profile, allRatings ->
                val profileName = profile?.nombre?.trim().orEmpty()
                val resolvedName = when {
                    profileName.isNotBlank() -> profileName
                    emailAlias.isNotBlank() -> emailAlias
                    else -> "Guía UVG"
                }

                val myRatings = allRatings.filter { item ->
                    matchesCurrentGuide(
                        rawEntry = item.nombre,
                        profileName = profileName,
                        emailAlias = emailAlias
                    )
                }

                val promedio = if (myRatings.isNotEmpty()) {
                    myRatings.map { it.calificacion }.average()
                } else {
                    0.0
                }

                val recientes = myRatings.reversed().map { parseRatingDetail(it) }

                ScoresState(
                    guideName = resolvedName,
                    guideEmail = email,
                    promedioTotal = promedio,
                    totalEvaluaciones = myRatings.size,
                    calificacionesRecientes = recientes,
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
                .collect { updatedState ->
                    _uiState.value = updatedState
                }
        }
    }

    private fun matchesCurrentGuide(
        rawEntry: String,
        profileName: String,
        emailAlias: String
    ): Boolean {
        val ratedGuide = rawEntry
            .substringBefore(" — ")
            .substringBefore(" (")
            .trim()

        if (ratedGuide.isBlank()) return false

        val matchesProfile = profileName.isNotBlank() && (
                ratedGuide.equals(profileName, ignoreCase = true) ||
                        profileName.split(" ").any { part ->
                            part.length >= 3 && part.equals(ratedGuide, ignoreCase = true)
                        }
                )

        val matchesEmail = emailAlias.isNotBlank() &&
                ratedGuide.equals(emailAlias, ignoreCase = true)

        return matchesProfile || matchesEmail
    }

    private fun parseRatingDetail(entry: calificacionGuia): GuiaRatingItem {
        val raw = entry.nombre
        val detailPart = raw.substringAfter(" — ", missingDelimiterValue = "").trim()

        if (detailPart.isBlank()) {
            return GuiaRatingItem(
                tourNombre = "Recorrido en Campus",
                comentario = "",
                calificacion = entry.calificacion
            )
        }

        return if (detailPart.endsWith(")") && detailPart.contains(" (")) {
            val tour = detailPart.substringBeforeLast(" (").trim()
            val comment = detailPart.substringAfterLast(" (").removeSuffix(")").trim()
            GuiaRatingItem(
                tourNombre = tour.ifBlank { "Recorrido en Campus" },
                comentario = comment,
                calificacion = entry.calificacion
            )
        } else {
            GuiaRatingItem(
                tourNombre = detailPart,
                comentario = "",
                calificacion = entry.calificacion
            )
        }
    }
}
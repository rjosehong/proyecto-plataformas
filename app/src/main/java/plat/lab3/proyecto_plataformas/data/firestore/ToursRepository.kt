package plat.lab3.proyecto_plataformas.data.firestore

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import plat.lab3.proyecto_plataformas.data.ejemplos.Admin
import plat.lab3.proyecto_plataformas.data.ejemplos.CuadroHoras
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import plat.lab3.proyecto_plataformas.data.ejemplos.calificacionGuia

object ToursRepository {

    private val db get() = FirebaseFirestore.getInstance()

    fun tours(): Flow<List<Tour>> =
        collectionFlow(Collections.TOURS, DocumentSnapshot::toTour) { list -> list.sortedBy { it.tourId } }

    fun horas(): Flow<List<CuadroHoras>> =
        collectionFlow(Collections.HORAS, DocumentSnapshot::toCuadroHoras)

    fun calificaciones(): Flow<List<calificacionGuia>> =
        collectionFlow(Collections.CALIFICACIONES, DocumentSnapshot::toCalificacionGuia)

    fun adminResumen(): Flow<List<Admin>> =
        collectionFlow(Collections.ADMIN, DocumentSnapshot::toAdmin)

    fun createTour(tour: Tour, onResult: (Result<Unit>) -> Unit) {
        db.collection(Collections.TOURS)
            .document(tour.tourId.toString())
            .set(tour.toMap())
            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun claimGuideSlot(tourId: Int, guideName: String, onResult: (Result<Unit>) -> Unit) {
        db.collection(Collections.TOURS)
            .document(tourId.toString())
            .update(
                mapOf(
                    "guia_necesitado" to 0,
                    "persona_asignada" to guideName
                )
            )
            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun registerUserToTour(tourId: Int, onResult: (Result<Unit>) -> Unit) {
        db.collection(Collections.TOURS)
            .document(tourId.toString())
            .update("personas_registradas", FieldValue.increment(1))
            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun saveCalificacion(calificacion: calificacionGuia, onResult: (Result<Unit>) -> Unit) {
        val id = "calificacion_${System.currentTimeMillis()}"
        FirebaseFirestore.getInstance().collection(Collections.CALIFICACIONES)
            .document(id)
            .set(calificacion.toMap())
            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun saveCuadroHoras(cuadro: CuadroHoras, onResult: (Result<Unit>) -> Unit) {
        val id = "horas_${System.currentTimeMillis()}"
        FirebaseFirestore.getInstance().collection(Collections.HORAS)
            .document(id)
            .set(cuadro.toMap())
            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    private fun <T> collectionFlow(
        collection: String,
        mapper: (DocumentSnapshot) -> T,
        order: (List<T>) -> List<T> = { it }
    ): Flow<List<T>> = callbackFlow {
        val registration = db
            .collection(collection)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(order(snapshot.documents.map(mapper)))
                }
            }
        awaitClose { registration.remove() }
    }
}

fun firestoreErrorMessage(e: Throwable): String = when {
    e is FirebaseFirestoreException &&
            e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED ->
        "No tienes permiso para ver estos datos. Inicia sesión de nuevo."
    e is FirebaseNetworkException || (e is FirebaseFirestoreException &&
            e.code == FirebaseFirestoreException.Code.UNAVAILABLE) ->
        "Sin conexión a internet. Intenta de nuevo."
    else -> e.localizedMessage ?: "Error al cargar los datos"
}
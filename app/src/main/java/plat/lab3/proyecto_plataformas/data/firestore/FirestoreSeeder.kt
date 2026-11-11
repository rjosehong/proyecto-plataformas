package plat.lab3.proyecto_plataformas.data.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemploAdminView
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemploCalificacion
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemploHoras
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemplosDeTours
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
object FirestoreSeeder {

    suspend fun seedIfEmpty() {
        val db = FirebaseFirestore.getInstance()
        try {
            seed(db, Collections.TOURS,
                ejemplosDeTours.map { it.tourId.toString() to it.toMap() })
            seed(db, Collections.HORAS,
                ejemploHoras.mapIndexed { i, h -> "horas_${i + 1}" to h.toMap() })
            seed(db, Collections.CALIFICACIONES,
                ejemploCalificacion.mapIndexed { i, c -> "calificacion_${i + 1}" to c.toMap() })
            seed(db, Collections.ADMIN,
                ejemploAdminView.mapIndexed { i, a -> "resumen_${i + 1}" to a.toMap() })
        } catch (e: Exception) {
            Log.w("FirestoreSeeder", "No se pudieron cargar los datos iniciales", e)
        }
    }

    private suspend fun seed(
        db: FirebaseFirestore,
        collection: String,
        docs: List<Pair<String, Map<String, Any>>>
    ) {
        val isEmpty = db.collection(collection).limit(1).get().awaitResult().isEmpty
        if (!isEmpty) return

        val batch = db.batch()
        docs.forEach { (id, data) ->
            batch.set(db.collection(collection).document(id), data)
        }
        batch.commit().awaitResult()
    }
}

private suspend fun <T> Task<T>.awaitResult(): T = suspendCancellableCoroutine { cont ->
    addOnSuccessListener { cont.resume(it) }
    addOnFailureListener { cont.resumeWithException(it) }
}

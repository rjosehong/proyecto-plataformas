package plat.lab3.proyecto_plataformas.data.firestore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val USUARIOS = "usuarios"

data class UserProfile(
    val nombre: String = "",
    val carne: String = ""
)

object UsersRepository {

    private val db get() = FirebaseFirestore.getInstance()

    fun saveProfile(uid: String, email: String, nombre: String, carne: String) {
        db.collection(USUARIOS).document(uid)
            .set(
                mapOf(
                    "nombre" to nombre.trim(),
                    "carne" to carne.trim(),
                    "email" to email.trim()
                )
            )
            .addOnFailureListener { Log.w("UsersRepository", "No se pudo guardar el perfil", it) }
    }

    fun profile(uid: String): Flow<UserProfile?> = callbackFlow {
        val registration = db.collection(USUARIOS).document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val profile = if (snapshot != null && snapshot.exists()) {
                    UserProfile(
                        nombre = snapshot.getString("nombre").orEmpty(),
                        carne = snapshot.getString("carne").orEmpty()
                    )
                } else {
                    null
                }
                trySend(profile)
            }
        awaitClose { registration.remove() }
    }
}

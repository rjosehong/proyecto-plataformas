package plat.lab3.proyecto_plataformas.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import plat.lab3.proyecto_plataformas.data.firestore.UsersRepository
import plat.lab3.proyecto_plataformas.navigation.Role
private val adminEmails = setOf("admin@uvg.edu.gt")
private val guideEmails = setOf("guia@uvg.edu.gt")

fun roleForEmail(email: String): Role = when (email.trim().lowercase()) {
    in adminEmails -> Role.ADMIN
    in guideEmails -> Role.TOUR_GUIDE
    else -> Role.USER
}

object AuthRepository {

    private val auth: FirebaseAuth get() = FirebaseAuth.getInstance()

    fun currentEmail(): String? = auth.currentUser?.email

    fun currentUid(): String? = auth.currentUser?.uid

    fun currentRole(): Role? = currentEmail()?.let { roleForEmail(it) }

    fun login(email: String, password: String, onResult: (Result<Role>) -> Unit) {
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { onResult(Result.success(roleForEmail(email))) }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun register(
        email: String,
        password: String,
        nombre: String,
        carne: String,
        onResult: (Result<Role>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { result ->
                result.user?.uid?.let { uid ->
                    UsersRepository.saveProfile(uid, email, nombre, carne)
                }
                onResult(Result.success(roleForEmail(email)))
            }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun logout() {
        auth.signOut()
    }
}

fun authErrorMessage(e: Throwable): String = when (e) {
    is FirebaseAuthInvalidUserException,
    is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña inválidos"
    is FirebaseAuthUserCollisionException -> "Ese correo ya está registrado"
    is FirebaseAuthWeakPasswordException -> "La contraseña debe tener al menos 6 caracteres"
    is FirebaseNetworkException -> "Sin conexión a internet. Intenta de nuevo."
    else -> e.localizedMessage ?: "No se pudo completar la operación."
}

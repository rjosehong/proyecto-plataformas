package plat.lab3.proyecto_plataformas.data.calendar

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.calendar.CalendarScopes

@Composable
fun rememberCalendarAuthorizer(
    onToken: (String) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val currentOnToken = rememberUpdatedState(onToken)
    val currentOnError = rememberUpdatedState(onError)

    val consentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val authorization = Identity.getAuthorizationClient(context)
                .getAuthorizationResultFromIntent(result.data)
            val token = authorization.accessToken
            if (token != null) {
                currentOnToken.value(token)
            } else {
                currentOnError.value("Google no entregó el acceso al calendario")
            }
        } catch (e: ApiException) {
            currentOnError.value("No se concedió el acceso al calendario")
        }
    }

    return {
        val request = AuthorizationRequest.builder()
            .setRequestedScopes(listOf(Scope(CalendarScopes.CALENDAR_EVENTS)))
            .build()

        Identity.getAuthorizationClient(context)
            .authorize(request)
            .addOnSuccessListener { authorization ->
                if (authorization.hasResolution()) {
                    val pendingIntent = authorization.pendingIntent
                    if (pendingIntent != null) {
                        consentLauncher.launch(
                            IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                        )
                    }
                } else {
                    val token = authorization.accessToken
                    if (token != null) currentOnToken.value(token)
                }
            }
            .addOnFailureListener { e ->
                currentOnError.value("No se pudo conectar con Google: ${e.localizedMessage}")
            }
    }
}

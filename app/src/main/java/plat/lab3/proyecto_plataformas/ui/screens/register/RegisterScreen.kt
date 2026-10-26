package plat.lab3.proyecto_plataformas.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppRegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterDone: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Crea tu cuenta",
                fontSize = 48.sp,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    drawStyle = Stroke(width = 15f, join = StrokeJoin.Round)
                )
            )
            Spacer(modifier = Modifier.padding(16.dp))

            if (state.errorMessage != null) {
                Text(text = state.errorMessage!!, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
            }

            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(RegisterEvent.EmailChanged(it)) },
                singleLine = true,
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.padding(top = 4.dp))
            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(RegisterEvent.PasswordChanged(it)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    onClick = { viewModel.onEvent(RegisterEvent.SubmitRegister(onRegisterDone)) }
                ) {
                    Text("Registrarse")
                }
            }
        }
    }
}
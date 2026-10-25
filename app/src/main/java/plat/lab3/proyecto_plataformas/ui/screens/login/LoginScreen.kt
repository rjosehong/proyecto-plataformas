package plat.lab3.proyecto_plataformas.ui.screens.login

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab3.proyecto_plataformas.navigation.Role

@Composable
fun AppLoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onEnterClick: (Role) -> Unit,
    onRegisterClick: () -> Unit
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
                text = "UVG–Tours",
                fontSize = 60.sp,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    drawStyle = Stroke(width = 15f, join = StrokeJoin.Round)
                )
            )

            Spacer(modifier = Modifier.padding(top = 24.dp))

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    onClick = { viewModel.onEvent(LoginEvent.SubmitLogin(Role.USER, onEnterClick)) }
                ) {
                    Text("Entrar como usuario")
                }

                Spacer(modifier = Modifier.padding(top = 8.dp))

                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    onClick = { viewModel.onEvent(LoginEvent.SubmitLogin(Role.TOUR_GUIDE, onEnterClick)) }
                ) {
                    Text("Entrar como tourguide")
                }

                Spacer(modifier = Modifier.padding(top = 8.dp))

                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    onClick = { viewModel.onEvent(LoginEvent.SubmitLogin(Role.ADMIN, onEnterClick)) }
                ) {
                    Text("Entrar como admin")
                }
            }

            TextButton(
                modifier = Modifier.fillMaxWidth(0.4f),
                onClick = onRegisterClick
            ) {
                Text(
                    text = "Register",
                    fontSize = 12.sp
                )
            }
        }
    }
}
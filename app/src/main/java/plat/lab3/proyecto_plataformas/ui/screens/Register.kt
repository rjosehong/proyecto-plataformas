package plat.lab3.proyecto_plataformas.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import plat.lab3.proyecto_plataformas.ui.theme.ProyectoplataformasTheme

@Composable
fun VistaRegister(modifier: Modifier =Modifier){
    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            Text(
                text = "Crea tu cuenta",
                fontSize = 48.sp,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    drawStyle = Stroke(
                        width = 15f,
                        join = StrokeJoin.Round
                    )
                )
            )
            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text("Ingresa tu correo")
                },
                label = {
                    Text("Correo Electrónico")
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.padding(top = 4.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text("Ingresa tu Contraseña")
                },
                label = {
                    Text("Contraseña")
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))

            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(0.4f),
                onClick = {

                }) {
                Text("Register")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterPreview() {
    ProyectoplataformasTheme(darkTheme = true) {
        VistaRegister()
    }
}
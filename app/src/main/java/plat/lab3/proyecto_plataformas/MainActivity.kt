package plat.lab3.proyecto_plataformas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import plat.lab3.proyecto_plataformas.ui.theme.ProyectoplataformasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoplataformasTheme() {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {

}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ProyectoplataformasTheme() {
    }
}
package plat.lab3.proyecto_plataformas.ui.screens.estudiantes

import android.R.attr.fontWeight
import android.R.attr.lineHeight
import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import plat.lab3.proyecto_plataformas.data.ejemplos.calificacionGuia
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemploCalificacion
import plat.lab3.proyecto_plataformas.ui.theme.ProyectoplataformasTheme

@Composable
fun Calificaciones(cards: List<calificacionGuia>) {
    ScoresContent(cards = ejemploCalificacion)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoresContent(modifier: Modifier = Modifier, cards: List<calificacionGuia>){
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
                title = { Text("UVG–Tours") }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(top=8.dp, start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = "Tu calificación como guía",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 53.sp
            )
            LazyColumn(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cards) { card ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier
                            .width(400.dp)
                            .height(150.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Box(modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.onTertiary)
                            .clip(RoundedCornerShape(4.dp))){
                            Row(modifier = Modifier,
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Column(modifier = Modifier){
                                    Text("Calificación como guía de tour")
                                    Text("${card.calificacion} " + "/" + " 100")
                                }
                            }
                        }
                    }
                }
            }
        }
        }
}

@Composable
fun ScoresScreen(modifier: Modifier = Modifier, cards: List<calificacionGuia>){
    ScoresContent(cards = cards)
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ScoresPreview(){
    ProyectoplataformasTheme(darkTheme = false){
        ScoresContent(cards = ejemploCalificacion)
    }
}
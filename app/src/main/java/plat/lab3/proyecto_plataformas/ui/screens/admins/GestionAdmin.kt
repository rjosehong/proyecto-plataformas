package plat.lab3.proyecto_plataformas.ui.screens.admins

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import plat.lab3.proyecto_plataformas.data.ejemplos.Admin
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemploAdminView
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemploHoras
import plat.lab3.proyecto_plataformas.ui.theme.ProyectoplataformasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun gestionAdminContent(cards: List<Admin>){
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Horas por Asignar",
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
                            containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .width(400.dp)
                            .height(400.dp)
                            .align(Alignment.CenterHorizontally)
                    ){
                        Column(modifier = Modifier.fillMaxSize()){
                            Row(horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 8.dp)){
                                Box(modifier = Modifier
                                    .fillMaxWidth(0.5f)){
                                    Column(){
                                        Text("Tours semanales")
                                        Text(
                                            text = buildAnnotatedString {
                                                withStyle(SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)){
                                                    append("${card.tours_semanales}")
                                                }
                                                append(" programados")
                                            }
                                        )

                                    }
                                }
                                Box(modifier = Modifier
                                    .fillMaxWidth(0.5f)){
                                    Column(){
                                        Text("Tours sin asignar")
                                        Text(
                                            text = buildAnnotatedString {
                                                withStyle(SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)){
                                                    append("${card.tours_noAsignados}")
                                                }
                                                append(" por asignar")
                                            }
                                        )

                                    }
                                }
                            }
                            Row(horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 8.dp)){
                                Box(modifier = Modifier
                                    .fillMaxWidth(0.5f)){
                                    Column(){
                                        Text("Pendiente de asignar")
                                        Text(
                                            text = buildAnnotatedString {
                                                withStyle(SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)){
                                                    append("${card.tours_completos_por_asignar}")
                                                }
                                                append(" completos por asignar")
                                            }
                                        )

                                    }
                                }
                                Box(modifier = Modifier
                                    .fillMaxWidth(0.5f)){
                                    Column(){
                                        Text("Ambassadors")
                                        Text(
                                            text = buildAnnotatedString {
                                                withStyle(SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)){
                                                    append("${card.tours_semanales}")
                                                }
                                                append(" guías de tour")
                                            }
                                        )

                                    }
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
fun gestionAdmin(cards:List<Admin>){
    gestionAdminContent(cards = ejemploAdminView)
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun AdminPreview(){
    ProyectoplataformasTheme(darkTheme = false){
        gestionAdmin(cards = ejemploAdminView)
    }
}



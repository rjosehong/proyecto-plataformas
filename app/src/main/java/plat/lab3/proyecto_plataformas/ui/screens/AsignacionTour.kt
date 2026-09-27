package plat.lab3.proyecto_plataformas.ui.screens

import android.R.attr.onClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.R
import plat.lab3.proyecto_plataformas.ui.theme.ProyectoplataformasTheme

data class CuadroAsignacion(
    val horas:Double, val tour_nombre:String, val slot:Int, val dia:String,
    val persona_asignada:String, val guia_necesitado:Int){

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignacionTour(card: CuadroAsignacion, content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text("UVG–Tours", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                    HorizontalDivider()

                    NavigationDrawerItem(
                        label = { Text("Tours") },
                        selected = false,
                        onClick = { }
                    )
                    NavigationDrawerItem(
                        label = { Text("Estudiantes") },
                        selected = false,
                        onClick = { }
                    )

                    NavigationDrawerItem(
                        label = { Text("Administrador") },
                        selected = false,
                        onClick = { }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = { Text("UVG–Tours") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }
                        ) {
                            Icon(modifier = Modifier.fillMaxSize(0.8f),
                                painter = painterResource(id = R.drawable.icon_menu),
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top=8.dp, start = 8.dp, end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = "Asignate un Tour",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                LazyColumn(modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .fillMaxWidth()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier
                                .width(400.dp)
                                .height(150.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Row(modifier = Modifier){
                                Box(modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .wrapContentSize()
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(end=4.dp)){
                                    Text(
                                        text = "+${card.horas} horas de servicio",
                                        color = Color.White
                                    )
                                }
                                Box(modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .wrapContentSize()
                                    .background(MaterialTheme.colorScheme.tertiary)
                                    .padding(end=4.dp)){
                                    Text(
                                        text = "${card.slot} espacio disponible",
                                        color = Color.White
                                    )
                                }
                            }
                            Text(
                                text = "${card.tour_nombre}",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${card.dia}"
                            )
                            Row(modifier = Modifier
                                .fillMaxWidth(),
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Box(modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .wrapContentSize()
                                    .background(MaterialTheme.colorScheme.secondary)
                                    .padding(end=4.dp)
                                    .align(Alignment.CenterVertically)){
                                    if (card.guia_necesitado > 0){
                                        Text(
                                            text = "${card.guia_necesitado} Guia Necesitado",
                                            color = Color.White
                                        )
                                    } else if(card.guia_necesitado == 0) {
                                        Text(
                                            text = "${card.persona_asignada} Asignado",
                                            color = Color.White
                                        )
                                    }
                                }

                                Button(
                                    onClick = {

                                }) {
                                    Text(
                                        text = "Claim Slot"
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

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun AsignacionesPreview(){
    ProyectoplataformasTheme(darkTheme = false){
        AsignacionTour(card = CuadroAsignacion(
            horas = 2.0,
            tour_nombre = "Tour del CIT",
            slot = 1,
            dia = "Fri, Oct 02",
            persona_asignada = "Iosef",
            guia_necesitado = 0
        )){

        }
    }
}